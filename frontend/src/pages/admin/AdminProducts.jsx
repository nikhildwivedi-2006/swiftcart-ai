import { useEffect, useState } from "react";
import ProductForm from "../../components/admin/ProductForm";
import ProductTable from "../../components/admin/ProductTable";
import productService from "../../services/productService";
import toast from "react-hot-toast";

export default function AdminProducts() {
  const [products, setProducts] = useState([]);
  const [editingProduct, setEditingProduct] = useState(null);
  const [loading, setLoading] = useState(true);

  const loadProducts = async () => {
    try {
      const data = await productService.getAllProducts();
      setProducts(data);
    } catch (err) {
      toast.error("Failed to load products");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadProducts();
  }, []);

  return (
    <div className="container py-4">
      <h2 className="mb-4">Product Management</h2>

      <ProductForm
        editingProduct={editingProduct}
        refreshProducts={loadProducts}
        clearEditing={() => setEditingProduct(null)}
      />

      <hr className="my-4" />

      <ProductTable
        products={products}
        loading={loading}
        refreshProducts={loadProducts}
        onEdit={setEditingProduct}
      />
    </div>
  );
}
