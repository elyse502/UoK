// Prompt the cashier for item price and quantity
const price = parseFloat(prompt("Enter the price of the item (RWF):"));
const quantity = parseInt(prompt("Enter the quantity purchased:"));

// Calculate total cost
let total = price * quantity;

// Apply 10% discount if total exceeds 50,000 RWF
if (total > 50000) {
  const discount = total * 0.1;
  total = total - discount;
  alert(
    `A 10% discount has been applied!\n` +
      `Discount Amount: ${discount.toLocaleString()} RWF\n` +
      `Final Amount to Pay: ${total.toLocaleString()} RWF`,
  );
} else {
  alert(
    `No discount applied.\nTotal Amount to Pay: ${total.toLocaleString()} RWF`,
  );
}
