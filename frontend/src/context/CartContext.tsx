"use client";

import { createContext, useContext, useState, useEffect } from 'react';

// Create the context
const CartContext = createContext();

// Create a custom hook to use the cart context
export const useCart = () => useContext(CartContext);

/**
 * CartProvider component to wrap the application.
 * It manages the cart state and provides functions to interact with the cart.
 * The cart state includes:
 * - cartItems: An array of product objects in the cart.
 * - currentSellerId: The ID of the seller whose products are currently in the cart.
 */
export const CartProvider = ({ children }) => {
    // State to hold the products in the cart
    const [cartItems, setCartItems] = useState([]);
    // State to hold the ID of the seller for the current cart
    const [currentSellerId, setCurrentSellerId] = useState(null);

    // Calculate the total price of all items in the cart
    const totalAmount = cartItems.reduce((total, item) => total + (item.price * item.qtyInCart), 0);

    /**
     * Adds a product to the cart.
     * Implements single-seller logic: if the new product's seller doesn't match the current cart's seller,
     * the cart is cleared and a new cart is started with the new product.
     * @param {object} productToAdd - The product object to add.
     * @param {number} sellerId - The ID of the product's seller.
     */
    const addToCart = (productToAdd, sellerId) => {
        // If there's no current seller or the new seller is different, clear the cart and start a new one
        if (currentSellerId === null || currentSellerId !== sellerId) {
            console.log('Clearing old cart and starting a new one for a different seller.');
            setCurrentSellerId(sellerId);
            setCartItems([{ ...productToAdd, qtyInCart: 1 }]);
            return;
        }

        // The new product's seller matches the current cart's seller.
        setCartItems(prevItems => {
            const existingItemIndex = prevItems.findIndex(item => item.productId === productToAdd.productId);

            if (existingItemIndex !== -1) {
                // Product already exists in the cart, update its quantity.
                // We create a new array to ensure immutability.
                return prevItems.map((item, index) => {
                    if (index === existingItemIndex) {
                        // Check if the quantity will exceed the max available
                        const newQty = item.qtyInCart + 1;
                        if (newQty > productToAdd.maxQuantity) {
                            console.warn(`Cannot add more. Max quantity for ${item.name} is ${productToAdd.maxQuantity}.`);
                            return item; // Return the item without changing its quantity
                        }
                        return { ...item, qtyInCart: newQty };
                    }
                    return item;
                });
            } else {
                // Product is new to the cart, add it to the list.
                return [...prevItems, { ...productToAdd, qtyInCart: 1 }];
            }
        });
    };

    /**
     * Removes an item from the cart completely.
     * @param {number} productId - The ID of the product to remove.
     */
    const removeFromCart = (productId) => {
        setCartItems(prevItems => {
            const updatedItems = prevItems.filter(item => item.productId !== productId);
            // If the cart becomes empty, reset the seller ID as well
            if (updatedItems.length === 0) {
                setCurrentSellerId(null);
            }
            return updatedItems;
        });
    };

    /**
     * Updates the quantity of a product in the cart.
     * @param {number} productId - The ID of the product to update.
     * @param {number} quantity - The new quantity.
     */
    const updateQuantity = (productId, quantity) => {
        setCartItems(prevItems => {
            // If the new quantity is less than 1, remove the item
            if (quantity < 1) {
                const updatedItems = prevItems.filter(item => item.productId !== productId);
                // If the cart becomes empty, reset the seller ID as well
                if (updatedItems.length === 0) {
                    setCurrentSellerId(null);
                }
                return updatedItems;
            }
            const updatedItems = prevItems.map(item => {
                if (item.productId === productId) {
                    // Ensure quantity is within valid range
                    const newQty = Math.min(quantity, item.maxQuantity);
                    return { ...item, qtyInCart: newQty };
                }
                return item;
            });
            return updatedItems;
        });
    };

    /**
     * Clears the entire cart.
     */
    const clearCart = () => {
        setCartItems([]);
        setCurrentSellerId(null);
    };

    const value = {
        cartItems,
        currentSellerId,
        totalAmount,
        addToCart,
        removeFromCart,
        updateQuantity,
        clearCart
    };

    return (
        <CartContext.Provider value={value}>
            {children}
        </CartContext.Provider>
    );
};
