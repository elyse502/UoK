// Select the container using querySelector
const container = document.querySelector("#weather-container");

// Create the card div
const card = document.createElement("div");

// Create heading
const heading = document.createElement("h2");
heading.textContent = "Today's Weather";

// Create temperature paragraph
const temperature = document.createElement("p");
temperature.textContent = "28°C";

// Create description paragraph
const description = document.createElement("p");
description.textContent = "Sunny with light breeze";

// Create the "More Details" button
const button = document.createElement("button");
button.textContent = "More Details";

// Apply CSS styles to the card using JavaScript
card.style.backgroundColor = "#fffbe6";
card.style.padding = "30px";
card.style.borderRadius = "16px";
card.style.boxShadow = "0 4px 12px rgba(0, 0, 0, 0.15)";
card.style.maxWidth = "320px";
card.style.margin = "60px auto";
card.style.textAlign = "center";
card.style.fontFamily = "Arial, sans-serif";

heading.style.fontSize = "22px";
heading.style.fontWeight = "bold";
heading.style.color = "#333";
heading.style.marginBottom = "10px";

temperature.style.fontSize = "48px";
temperature.style.fontWeight = "bold";
temperature.style.color = "#2781f6";
temperature.style.margin = "10px 0";

description.style.fontSize = "15px";
description.style.color = "#666";
description.style.marginBottom = "20px";

button.style.backgroundColor = "#2781f6";
button.style.color = "#fff";
button.style.border = "none";
button.style.padding = "10px 24px";
button.style.borderRadius = "8px";
button.style.fontSize = "14px";
button.style.cursor = "pointer";
button.style.fontWeight = "bold";

// Hover effects using mouse events
button.addEventListener("mouseover", () => {
  button.style.backgroundColor = "#1206b6";
});
button.addEventListener("mouseout", () => {
  button.style.backgroundColor = "#2781f6";
});

// Event listener for the button click
button.addEventListener("click", () => {
  alert("Loading detailed forecast...");
});

// Append all elements into the card, then card into container
card.appendChild(heading);
card.appendChild(temperature);
card.appendChild(description);
card.appendChild(button);
container.appendChild(card);
