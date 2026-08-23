import { useState } from "react";
import toast from "react-hot-toast";
import productService from "../../services/productService";

export default function ProductTable({ products, refreshProducts, onEdit }) {
  const [search, setSearch] = useState("");

  const filteredProducts = products.filter((product) =>
    product.name.toLowerCase().includes(search.toLowerCase()),
  );

  const handleDelete = async (id) => {
    if (!window.confirm("Delete this product?")) return;

    try {
      await productService.deleteProduct(id);
      toast.success("Product Deleted");
      refreshProducts();
    } catch (err) {
      console.error(err);
      toast.error("Delete Failed");
    }
  };

  return (
    <div>
      <h3>Products</h3>

      <input
        type="text"
        placeholder="Search Product..."
        value={search}
        onChange={(e) => setSearch(e.target.value)}
      />

      <table>
        <thead>
          <tr>
            <th>Image</th>
            <th>Name</th>
            <th>Category</th>
            <th>Brand</th>
            <th>Price</th>
            <th>Stock</th>
            <th>Available</th>
            <th>Action</th>
          </tr>
        </thead>

        <tbody>
          {filteredProducts.map((product) => (
            <tr key={product.id}>
              <td>
                <img
                  src={product.imageUrl}
                  alt={product.name}
                  width="60"
                  height="60"
                />
              </td>

              <td>{product.name}</td>

              <td>{product.category}</td>

              <td>{product.brand}</td>

              <td>₹ {product.price}</td>

              <td>{product.stockQuantity}</td>

              <td>{product.isAvailable ? "Yes" : "No"}</td>

              <td>
                <button onClick={() => onEdit(product)}>Edit</button>

                <button onClick={() => handleDelete(product.id)}>Delete</button>
              </td>
            </tr>
          ))}

          {filteredProducts.length === 0 && (
            <tr>
              <td colSpan="8">No Products Found</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}
