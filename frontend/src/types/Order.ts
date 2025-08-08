/**
 * Type definitions for Order API
 */

import { SellerInfo } from './ProductSeller';

export interface OrderRequestBody {
    userId: number;
    sellerId: number;
    sellerResponse: SellerInfo;
    productId: number[];
    quantity: number[];
    price: number;
}

export interface OrderResponseBody {
    orderId: number;
    userId: number;
    sellerId: number;
    sellerResponse: SellerInfo;
    productId: number[];
    quantity: number[];
    price: number;
    status: string;
    orderDate: string;
}

export interface PlaceOrderError {
    message: string;
    code?: string;
    details?: string;
}

export interface PlaceOrderResult {
    success: boolean;
    data?: string;
    error?: PlaceOrderError;
}
