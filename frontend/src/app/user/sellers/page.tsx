
"use client";

import { useState, useEffect } from "react";
import Link from "next/link";
import { useSearchParams, usePathname } from "next/navigation";
import HostelBitesLogo from "@/components/HostelBitesLogo";

// Mock sellers data
const generateSellers = (productName: string) => [
    {
        id: 1,
        name: "Rajesh's Kitchen",
        rating: 4.8,
        distance: "0.2 km",
        minQuantity: 1,
        maxQuantity: 50,
        prepTime: "15-20 min",
        profileImage: "👨‍🍳",
        speciality: "Fast Food & Snacks",
        reviews: 245
    },
    {
        id: 2,
        name: "Mama's Food Corner",
        rating: 4.6,
        distance: "0.5 km",
        minQuantity: 2,
        maxQuantity: 30,
        prepTime: "20-25 min",
        profileImage: "👩‍🍳",
        speciality: "Home Style Cooking",
        reviews: 189
    },
    {
        id: 3,
        name: "Hostel Canteen",
        rating: 4.3,
        distance: "0.1 km",
        minQuantity: 1,
        maxQuantity: 100,
        prepTime: "10-15 min",
        profileImage: "🏪",
        speciality: "Student Favorites",
        reviews: 567
    },
    {
        id: 4,
        name: "Quick Bites Express",
        rating: 4.7,
        distance: "0.8 km",
        minQuantity: 5,
        maxQuantity: 25,
        prepTime: "25-30 min",
        profileImage: "🚀",
        speciality: "Fast Delivery",
        reviews: 156
    },
    {
        id: 5,
        name: "Street Food Hub",
        rating: 4.4,
        distance: "0.3 km",
        minQuantity: 1,
        maxQuantity: 20,
        prepTime: "15-25 min",
        profileImage: "🏭",
        speciality: "Authentic Street Food",
        reviews: 298
    },
];

export default function Sellers() {
    const searchParams = useSearchParams();
    const pathname = usePathname();
    const [searchQuery, setSearchQuery] = useState("");
    const [quantityFilter, setQuantityFilter] = useState(1);
    const [sellers, setSellers] = useState<any[]>([]);
    const [filteredSellers, setFilteredSellers] = useState<any[]>([]);
    const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

    const productName = searchParams.get('product') || 'Product';
    const productPrice = searchParams.get('price') || '₹0';
    const productImage = searchParams.get('image') || '🍽️';

    useEffect(() => {
        const sellerData = generateSellers(productName);
        setSellers(sellerData);
        setFilteredSellers(sellerData);
    }, [productName]);

    const handleSearch = (query: string, quantity: number) => {
        setSearchQuery(query);
        setQuantityFilter(quantity);

        let filtered = sellers;

        // Filter by name
        if (query.trim() !== "") {
            filtered = filtered.filter(seller =>
                seller.name.toLowerCase().includes(query.toLowerCase()) ||
                seller.speciality.toLowerCase().includes(query.toLowerCase())
            );
        }

        // Filter by quantity capability
        filtered = filtered.filter(seller =>
            quantity >= seller.minQuantity && quantity <= seller.maxQuantity
        );

        setFilteredSellers(filtered);
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
                    {/* Back Button */}
                    <div className="mb-6">
                        <Link href="/userHome" className="inline-flex items-center text-gray-700 hover:text-blue-600 transition-colors">
                            <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
                            </svg>
                            Back to Products
                        </Link>
                    </div>

                    {/* Product Info Header */}
                    <div className="text-center mb-8">
                        <div className="backdrop-blur-lg bg-white/30 border border-white/20 rounded-2xl p-6 shadow-2xl shadow-black/10 max-w-md mx-auto">
                            <div className="text-4xl mb-3">{productImage}</div>
                            <h1 className="text-2xl font-bold text-gray-800 mb-2">{productName}</h1>
                            <p className="text-lg text-gray-600">Base Price: <span className="font-bold text-gray-800">{productPrice}</span></p>
                        </div>
                    </div>

                    {/* Search and Filter Section */}
                    <div className="max-w-4xl mx-auto mb-12">
                        <div className="backdrop-blur-lg bg-white/30 border border-white/20 rounded-2xl p-6 shadow-2xl shadow-black/10">
                            <h2 className="text-xl font-bold text-gray-800 mb-6 text-center">Find Sellers for {productName}</h2>

                            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 items-end">
                                {/* Search Bar */}
                                <div className="md:col-span-2">
                                    <label className="block text-sm font-medium text-gray-700 mb-2">Search by Name or Specialty</label>
                                    <div className="relative">
                                        <input
                                            type="text"
                                            placeholder="Search sellers by name or specialty..."
                                            value={searchQuery}
                                            onChange={(e) => handleSearch(e.target.value, quantityFilter)}
                                            className="w-full px-4 py-3 bg-white/20 backdrop-blur-sm border border-white/30 rounded-xl text-gray-800 placeholder-gray-600 focus:outline-none focus:ring-2 focus:ring-blue-500"
                                        />
                                    </div>
                                </div>

                                {/* Quantity Filter */}
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-2">Quantity Needed</label>
                                    <input
                                        type="number"
                                        min="1"
                                        max="100"
                                        value={quantityFilter}
                                        onChange={(e) => handleSearch(searchQuery, parseInt(e.target.value) || 1)}
                                        className="w-full px-4 py-3 bg-white/20 backdrop-blur-sm border border-white/30 rounded-xl text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500"
                                    />
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* Sellers Grid */}
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {filteredSellers.map((seller) => (
                            <div
                                key={seller.id}
                                className="group backdrop-blur-lg bg-white/30 border border-white/20 rounded-2xl p-6 shadow-2xl shadow-black/10 hover:shadow-black/20 transition-all duration-300 hover:scale-105 hover:bg-white/40"
                            >
                                {/* Seller Profile */}
                                <div className="text-center mb-4">
                                    <div className="text-4xl mb-3 group-hover:scale-110 transition-transform duration-300">
                                        {seller.profileImage}
                                    </div>
                                    <span className="inline-block bg-gradient-to-r from-blue-600 to-purple-600 text-white text-xs px-3 py-1 rounded-full font-medium">
                    {seller.speciality}
                  </span>
                                </div>

                                {/* Seller Info */}
                                <div className="text-center">
                                    <h3 className="text-xl font-bold text-gray-800 mb-2 group-hover:text-blue-600 transition-colors">
                                        {seller.name}
                                    </h3>

                                    {/* Stats */}
                                    <div className="grid grid-cols-2 gap-4 mb-4 text-sm text-gray-600">
                                        <div>
                                            <div className="flex items-center justify-center mb-1">
                                                <span className="text-yellow-500 mr-1">⭐</span>
                                                <span className="font-medium">{seller.rating}</span>
                                            </div>
                                            <div>{seller.reviews} reviews</div>
                                        </div>
                                        <div>
                                            <div className="font-medium text-gray-800">{seller.distance}</div>
                                            <div>{seller.prepTime}</div>
                                        </div>
                                    </div>

                                    {/* Quantity Info */}
                                    <div className="bg-white/20 backdrop-blur-sm rounded-xl p-3 mb-4">
                                        <div className="text-sm text-gray-600 mb-1">Quantity Range</div>
                                        <div className="font-bold text-gray-800">
                                            {seller.minQuantity} - {seller.maxQuantity} pieces
                                        </div>
                                    </div>

                                    {/* Action Buttons */}
                                    <div className="flex gap-2">
                                        <button className="flex-1 bg-gradient-to-r from-blue-600 to-indigo-600 text-white px-4 py-2 rounded-xl hover:from-blue-700 hover:to-indigo-700 transition-all duration-200 shadow-lg shadow-blue-500/25 text-sm font-medium">
                                            View Menu
                                        </button>
                                        <button className="flex-1 bg-gradient-to-r from-purple-500 to-purple-600 text-white px-4 py-2 rounded-xl hover:from-purple-600 hover:to-purple-700 transition-all duration-200 shadow-lg shadow-purple-500/25 text-sm font-medium">
                                            Order Now
                                        </button>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>

                    {/* No Results */}
                    {filteredSellers.length === 0 && (
                        <div className="text-center py-12">
                            <div className="backdrop-blur-lg bg-white/30 border border-white/20 rounded-2xl p-8 shadow-2xl shadow-black/10 max-w-md mx-auto">
                                <div className="text-6xl mb-4">🔍</div>
                                <h3 className="text-xl font-bold text-gray-800 mb-2">No sellers found</h3>
                                <p className="text-gray-600 mb-4">Try adjusting your search criteria or quantity needed</p>
                                <button
                                    onClick={() => {
                                        setSearchQuery("");
                                        setQuantityFilter(1);
                                        setFilteredSellers(sellers);
                                    }}
                                    className="bg-gradient-to-r from-blue-600 to-indigo-600 text-white px-6 py-3 rounded-xl hover:from-blue-700 hover:to-indigo-700 transition-all duration-200 shadow-lg shadow-blue-500/25"
                                >
                                    Reset Filters
                                </button>
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}
