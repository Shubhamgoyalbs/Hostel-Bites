"use client";

import {useEffect, useState} from "react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import HostelBitesLogo from "@/components/HostelBitesLogo";
import {Product} from "@/types/Product";
import axios from "axios";
import {useAuth} from "@/context/AuthContext";

export default function UserHome() {
    const [searchQuery, setSearchQuery] = useState("");
    const [products, setProducts] = useState<Product[]>([])
    const [filteredProducts, setFilteredProducts] = useState(products);
    const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
    const pathname = usePathname();
    const {token,logout} = useAuth();

    useEffect(() => {

        if (!token){
            logout();
        }

        const fetchProducts = async () => {
            console.log(`Bearer ${token}`);

            try {
                const response = await axios.get("http://localhost:8080/api/user/products/all", {
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${token}`,
                    },
                });


                console.log(response);

                if (response.status == 200 || response.status == 201) {
                    console.log("Fetched products:", response.data); // ✅ Check this shows correct array
                    setProducts(response.data);
                    setFilteredProducts(response.data);
                } else if (response.status === 403 || response.status === 401) {
                    logout();
                }
            } catch (error) {
                console.log("Failed to fetch products" , error);
                console.error(error);
            }
        };

        if (token) {
            fetchProducts();
        }
    }, [token]);

    const handleSearch = (query: string) => {
        setSearchQuery(query);
        if (query.trim() === "") {
            setFilteredProducts(products);
        } else {
            const filtered = products.filter(product =>
                product.name.toLowerCase().includes(query.toLowerCase()));
            setFilteredProducts(filtered);
        }
    };

    const NavLink = ({ href, children, isMobile = false }: { href: string; children: React.ReactNode; isMobile?: boolean }) => {
        const isActive = pathname === href;
        return (
            <Link
                href={href}
                className={`font-medium transition-colors relative group ${
                    isMobile ? 'block py-2 px-2 rounded-lg hover:bg-white/40' : ''
                } ${
                    isActive ? 'text-blue-600' : 'text-gray-700 hover:text-blue-600'
                }`}
            >
                {isMobile ? (
                    children
                ) : (
                    <span className="relative">
            {children}
                        <span className={`absolute bottom-0 left-0 h-0.5 bg-blue-600 transition-all duration-300 ${
                            isActive ? 'w-full' : 'w-0 group-hover:w-full'
                        }`}></span>
          </span>
                )}
            </Link>
        );
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-blue-50 via-indigo-50 to-purple-50 relative overflow-hidden">
            {/* Background decorative elements */}
            <div className="absolute inset-0 overflow-hidden">
                <div className="absolute -top-40 -right-40 w-80 h-80 bg-gradient-to-br from-blue-400/20 to-purple-400/20 rounded-full blur-3xl"></div>
                <div className="absolute -bottom-40 -left-40 w-80 h-80 bg-gradient-to-tr from-indigo-400/20 to-pink-400/20 rounded-full blur-3xl"></div>
                <div className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 w-96 h-96 bg-gradient-to-r from-cyan-400/10 to-blue-400/10 rounded-full blur-3xl"></div>
            </div>

            {/* Fixed Navbar */}
            <nav className="fixed top-3 left-3 right-3 z-50 backdrop-blur-xl bg-white/20 border border-white/30 rounded-3xl p-5 shadow-[0_8px_32px_0_rgba(31,38,135,0.37)] backdrop-saturate-200">
                <div className="flex items-center justify-between">
                    {/* Logo */}
                    <div className="flex items-center">
                        <HostelBitesLogo className="h-8 w-auto" />
                    </div>

                    {/* Desktop Navigation Links */}
                    <div className="hidden md:flex absolute left-1/2 transform -translate-x-1/2 items-center space-x-8">
                        <NavLink href="/user/home">Home</NavLink>
                        <NavLink href="/login">Login</NavLink>
                        <NavLink href="/user/orders">Orders</NavLink>
                    </div>

                    {/* Desktop Right Side - Cart and Profile */}
                    <div className="hidden md:flex items-center space-x-3">
                        <Link href="/user/cart" className="flex items-center justify-center w-10 h-10 bg-white/30 backdrop-blur-sm rounded-xl hover:bg-white/40 transition-all duration-200 group border border-white/20">
                            <svg className="w-5 h-5 text-gray-700 group-hover:text-blue-600 transition-colors" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 3h2l.4 2M7 13h10l4-8H5.4m0 0L7 13m0 0l-1.68 3.4a1 1 0 00.9 1.6h9.78M7 13v6a2 2 0 002 2h4a2 2 0 002-2v-6m-6 0a2 2 0 012-2h2a2 2 0 012 2" />
                            </svg>
                        </Link>
                        <Link href="/user/profile" className="flex items-center group">
                            <div className="w-10 h-10 bg-gradient-to-r from-blue-500 to-purple-500 rounded-xl flex items-center justify-center text-white font-bold group-hover:scale-105 transition-transform duration-200 shadow-lg">
                                👤
                            </div>
                        </Link>
                    </div>

                    {/* Mobile Hamburger Menu */}
                    <button
                        onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
                        className="md:hidden flex items-center justify-center w-10 h-10 bg-white/30 backdrop-blur-sm rounded-xl hover:bg-white/40 transition-all duration-200 border border-white/20"
                    >
                        <svg className="w-6 h-6 text-gray-700" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            {isMobileMenuOpen ? (
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                            ) : (
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
                            )}
                        </svg>
                    </button>
                </div>

                {/* Mobile Menu Overlay */}
                {isMobileMenuOpen && (
                    <div className="md:hidden absolute top-full right-0 mt-2 w-48 backdrop-blur-xl bg-white/90 border border-white/30 rounded-2xl shadow-[0_8px_32px_0_rgba(31,38,135,0.37)] backdrop-saturate-200 z-50">
                        <div className="p-3 space-y-1">
                            <NavLink href="/user/home" isMobile={true}>Home</NavLink>
                            <NavLink href="/login" isMobile={true}>Login</NavLink>
                            <NavLink href="/register" isMobile={true}>Register</NavLink>
                            <NavLink href="/user/orders" isMobile={true}>Orders</NavLink>

                            <Link href="/user/cart" className="flex items-center space-x-2 text-gray-700 hover:text-blue-600 transition-colors font-medium py-2 px-2 rounded-lg hover:bg-white/40">
                                <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 3h2l.4 2M7 13h10l4-8H5.4m0 0L7 13m0 0l-1.68 3.4a1 1 0 00.9 1.6h9.78M7 13v6a2 2 0 002 2h4a2 2 0 002-2v-6m-6 0a2 2 0 012-2h2a2 2 0 012 2" />
                                </svg>
                                <span>Cart</span>
                            </Link>

                            <Link href="/user/profile" className="flex items-center space-x-2 text-gray-700 hover:text-blue-600 transition-colors font-medium py-2 px-2 rounded-lg hover:bg-white/40">
                                <div className="w-4 h-4 bg-gradient-to-r from-blue-500 to-purple-500 rounded flex items-center justify-center text-white text-xs font-bold">
                                    👤
                                </div>
                                <span>Profile</span>
                            </Link>
                        </div>
                    </div>
                )}
            </nav>

            {/* Main Content */}
            <div className="pt-24 pb-8 px-4 sm:px-6 lg:px-8">
                <div className="max-w-7xl mx-auto">
                    {/* App Name with Effect */}
                    <div className="text-center mb-12">
                        <h1 className="text-6xl font-extrabold mb-4 bg-gradient-to-r from-blue-600 via-purple-600 to-indigo-600 bg-clip-text text-transparent animate-pulse">
                            HostelBites
                        </h1>
                        <p className="text-xl text-gray-700 backdrop-blur-sm bg-white/20 rounded-2xl px-6 py-3 inline-block border border-white/30">
                            Delicious food delivered to your hostel
                        </p>
                    </div>

                    {/* Search Bar */}
                    <div className="max-w-2xl mx-auto mb-12">
                        <div className="relative backdrop-blur-lg bg-white/30 border border-white/20 rounded-2xl p-2 shadow-2xl shadow-black/10">
                            <div className="flex items-center">
                                <div className="flex-shrink-0 pl-4">
                                    <svg
                                        className="h-6 w-6 text-gray-600"
                                        fill="none"
                                        stroke="currentColor"
                                        viewBox="0 0 24 24"
                                    >
                                        <path
                                            strokeLinecap="round"
                                            strokeLinejoin="round"
                                            strokeWidth={2}
                                            d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                                        />
                                    </svg>
                                </div>
                                <input
                                    type="text"
                                    placeholder="Search for delicious food..."
                                    value={searchQuery}
                                    onChange={(e) => handleSearch(e.target.value)}
                                    className="w-full pl-4 pr-4 py-4 bg-transparent text-gray-800 placeholder-gray-600 focus:outline-none text-lg"
                                />
                                <button className="flex-shrink-0 bg-gradient-to-r from-blue-600 to-indigo-600 text-white px-6 py-3 rounded-xl hover:from-blue-700 hover:to-indigo-700 transition-all duration-200 shadow-lg shadow-blue-500/25 font-medium">
                                    Search
                                </button>
                            </div>
                        </div>
                    </div>

                    {/* Products Grid */}
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                        {filteredProducts.map((product) => (
                            <div
                                key={product.productId}
                                className="group backdrop-blur-lg bg-white/30 border border-white/20 rounded-2xl p-6 shadow-2xl shadow-black/10 hover:shadow-black/20 transition-all duration-300 hover:scale-105 hover:bg-white/40"
                            >
                                {/* Product Image */}
                                <div className="text-center mb-4">
                                    <div className="text-6xl mb-3 group-hover:scale-110 transition-transform duration-300">
                                        {product.imageUrl}
                                    </div>
                                </div>

                                {/* Product Info */}
                                <div className="text-center">
                                    <h3 className="text-xl font-bold text-gray-800 mb-2 group-hover:text-blue-600 transition-colors">
                                        {product.name}
                                    </h3>
                                    <p className="text-gray-600 text-sm mb-3 line-clamp-2">
                                        {product.description}
                                    </p>

                                    {/* Price and See Sellers Button */}
                                    <div className="flex items-center justify-between">
                    <span className="text-2xl font-bold text-gray-800">
                      {product.price}
                    </span>
                                        <Link href={`/user/sellers?product=${encodeURIComponent(product.name)}&price=${encodeURIComponent(product.price)}&image=${encodeURIComponent(product.imageUrl)}`} className="bg-gradient-to-r from-purple-500 to-purple-600 text-white px-4 py-2 rounded-xl hover:from-purple-600 hover:to-purple-700 transition-all duration-200 shadow-lg shadow-purple-500/25 font-medium">
                                            See Sellers
                                        </Link>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>

                    {/* No Results */}
                    {filteredProducts.length === 0 && (
                        <div className="text-center py-12">
                            <div className="backdrop-blur-lg bg-white/30 border border-white/20 rounded-2xl p-8 shadow-2xl shadow-black/10 max-w-md mx-auto">
                                <div className="text-6xl mb-4">😕</div>
                                <h3 className="text-xl font-bold text-gray-800 mb-2">No products found</h3>
                                <p className="text-gray-600">Try searching with different keywords</p>
                                <button
                                    onClick={() => handleSearch("")}
                                    className="mt-4 bg-gradient-to-r from-blue-600 to-indigo-600 text-white px-6 py-2 rounded-xl hover:from-blue-700 hover:to-indigo-700 transition-all duration-200 shadow-lg shadow-blue-500/25"
                                >
                                    Show All Products
                                </button>
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}