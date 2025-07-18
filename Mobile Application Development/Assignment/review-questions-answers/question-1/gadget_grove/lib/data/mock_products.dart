import '../models/product.dart';

final List<Product> mockProducts = [
  Product(
    id: 'p1',
    name: 'Smart Watch X1',
    brand: 'TechWear',
    price: 199.99,
    imageUrl:
        'https://images.unsplash.com/photo-1565450115751-5f2e5de23d7b?q=80&w=1331&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    inStock: true,
  ),
  Product(
    id: 'p2',
    name: 'Wireless Earbuds Z5',
    brand: 'AudioPhonic',
    price: 129.99,
    imageUrl:
        'https://images.unsplash.com/photo-1733556046364-46a0804a6b94?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    inStock: true,
  ),
  Product(
    id: 'p3',
    name: '4K Action Camera',
    brand: 'GoExplore',
    price: 259.00,
    imageUrl:
        'https://images.unsplash.com/photo-1733218430878-902cc0cff502?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    inStock: false,
  ),
  Product(
    id: 'p4',
    name: 'Smart Drone Pro',
    brand: 'SkyTech',
    price: 499.99,
    imageUrl:
        'https://images.unsplash.com/photo-1661936955098-b991c99fd023?q=80&w=1074&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    inStock: true,
  ),
  Product(
    id: 'p5',
    name: 'Portable Bluetooth Speaker',
    brand: 'SoundRush',
    price: 89.50,
    imageUrl:
        'https://images.unsplash.com/photo-1674303324806-7018a739ed11?q=80&w=1074&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    inStock: true,
  ),
  Product(
    id: 'p6',
    name: 'Smart Glasses VisionX',
    brand: 'EyeGadget',
    price: 329.75,
    imageUrl:
        'https://images.unsplash.com/photo-1694010326911-b9e9947684ae?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    inStock: false,
  ),

  Product(
    id: 'p7',
    name: 'Gaming Headset UltraBass',
    brand: 'GamerZone',
    price: 149.99,
    imageUrl:
        'https://plus.unsplash.com/premium_photo-1682088499057-0b0263936f67?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    inStock: true,
  ),
  Product(
    id: 'p8',
    name: 'Fitness Tracker Pro',
    brand: 'FitLife',
    price: 109.99,
    imageUrl:
        'https://plus.unsplash.com/premium_photo-1712761997182-45455a50d8c4?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    inStock: true,
  ),
  Product(
    id: 'p9',
    name: 'Smartphone G12',
    brand: 'NextGen',
    price: 749.00,
    imageUrl:
        'https://images.unsplash.com/photo-1614918848092-b62132d8f3cc?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    inStock: false,
  ),
  Product(
    id: 'p10',
    name: 'Laptop Docking Station',
    brand: 'DockPro',
    price: 189.50,
    imageUrl:
        'https://images.unsplash.com/photo-1746202067382-42f129dbbe6b?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    inStock: true,
  ),
  Product(
    id: 'p11',
    name: 'Noise Cancelling Headphones',
    brand: 'QuietSound',
    price: 299.00,
    imageUrl:
        'https://images.unsplash.com/photo-1612478120679-5b7412e15f84?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    inStock: true,
  ),
  Product(
    id: 'p12',
    name: 'Wireless Charging Pad',
    brand: 'ChargeEase',
    price: 45.00,
    imageUrl:
        'https://images.unsplash.com/photo-1543472750-506bacbf5808?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    inStock: true,
  ),
  Product(
    id: 'p13',
    name: '360° Security Camera',
    brand: 'SecureCam',
    price: 129.00,
    imageUrl:
        'https://images.unsplash.com/photo-1683821291931-068148564d8c?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    inStock: true,
  ),
  Product(
    id: 'p14',
    name: 'Wireless Mechanical Keyboard',
    brand: 'KeyNinja',
    price: 159.99,
    imageUrl:
        'https://images.unsplash.com/photo-1679533662330-457ca8447e7d?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    inStock: true,
  ),
  Product(
    id: 'p15',
    name: 'LED Desk Lamp with USB',
    brand: 'BrightLite',
    price: 59.99,
    imageUrl:
        'https://images.unsplash.com/photo-1612813434847-b01fffea46ae?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    inStock: false,
  ),
  Product(
    id: 'p16',
    name: 'Smart Thermostat',
    brand: 'HomeTech',
    price: 199.99,
    imageUrl:
        'https://images.unsplash.com/photo-1545259741-2ea3ebf61fa3?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    inStock: true,
  ),
  Product(
    id: 'p17',
    name: 'Robot Vacuum Cleaner',
    brand: 'CleanBot',
    price: 299.99,
    imageUrl:
        'https://images.unsplash.com/photo-1603618090561-412154b4bd1b?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    inStock: true,
  ),
  Product(
    id: 'p18',
    name: 'Smart LED Strip Lights',
    brand: 'LightUp',
    price: 49.99,
    imageUrl:
        'https://images.unsplash.com/photo-1707386288144-034e25c873d3?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    inStock: true,
  ),
  Product(
    id: 'p19',
    name: 'Wireless Security System',
    brand: 'SecureHome',
    price: 399.99,
    imageUrl:
        'https://images.unsplash.com/photo-1724343025504-3afb6d67566b?q=80&w=944&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    inStock: true,
  ),
  Product(
    id: 'p20',
    name: 'Smart Door Lock',
    brand: 'LockTech',
    price: 149.99,
    imageUrl:
        'https://images.unsplash.com/photo-1558002038-1055907df827?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
    inStock: true,
  ),
];
