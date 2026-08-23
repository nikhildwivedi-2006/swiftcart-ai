import { useEffect, useState } from "react";
import productService from "../../services/productService";
import toast from "react-hot-toast";

const initialForm = {
  name: "",
  description: "",
  price: "",
  stockQuantity: "",
  category: "",
  brand: "",
  sku: "",
  imageUrl: "",
  isAvailable: true,
};

export default function ProductForm({
  editingProduct,
  refreshProducts,
  clearEditing,
}) {
  const [formData, setFormData] = useState(initialForm);

  useEffect(() => {
    if (editingProduct) {
      setFormData(editingProduct);
    } else {
      setFormData(initialForm);
    }
  }, [editingProduct]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;

    setFormData({
      ...formData,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      if (editingProduct) {
        await productService.updateProduct(editingProduct.id, formData);
        toast.success("Product Updated");
      } else {
        await productService.createProduct(formData);
        toast.success("Product Added");
      }

      setFormData(initialForm);
      clearEditing();
      refreshProducts();
    } catch (err) {
      toast.error("Something went wrong");
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="text"
        name="name"
        placeholder="Product Name"
        value={formData.name}
        onChange={handleChange}
      />

      <input
        type="text"
        name="brand"
        placeholder="Brand"
        value={formData.brand}
        onChange={handleChange}
      />

      <input
        type="text"
        name="category"
        placeholder="Category"
        value={formData.category}
        onChange={handleChange}
      />

      <input
        type="number"
        name="price"
        placeholder="Price"
        value={formData.price}
        onChange={handleChange}
      />

      <input
        type="number"
        name="stockQuantity"
        placeholder="Stock"
        value={formData.stockQuantity}
        onChange={handleChange}
      />

      <input
        type="text"
        name="sku"
        placeholder="SKU"
        value={formData.sku}
        onChange={handleChange}
      />

      <input
        type="text"
        name="imageUrl"
        placeholder="Image URL"
        value={formData.imageUrl}
        onChange={handleChange}
      />

      <textarea
        name="description"
        placeholder="Description"
        value={formData.description}
        onChange={handleChange}
      />

      <label>
        <input
          type="checkbox"
          name="isAvailable"
          checked={formData.isAvailable}
          onChange={handleChange}
        />
        Available
      </label>

      <button type="submit">
        {editingProduct ? "Update Product" : "Add Product"}
      </button>

      {editingProduct && (
        <button
          type="button"
          onClick={() => {
            clearEditing();
            setFormData(initialForm);
          }}
        >
          Cancel
        </button>
      )}
    </form>
  );
}
