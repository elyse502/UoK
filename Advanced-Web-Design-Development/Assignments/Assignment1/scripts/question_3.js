const lunchBtn = document.querySelector("#lunch-btn");
const modalOverlay = document.querySelector("#modal-overlay");
const closeBtn = document.querySelector("#close-btn");
const closeX = document.querySelector("#close-x");

// Show modal when Lunch button is clicked
lunchBtn.addEventListener("click", () => {
  modalOverlay.style.display = "flex";
});

// Hide modal when Close button (bottom) is clicked
closeBtn.addEventListener("click", () => {
  modalOverlay.style.display = "none";
});

// Hide modal when × (top right) is clicked
closeX.addEventListener("click", () => {
  modalOverlay.style.display = "none";
});

// Also close when clicking outside the modal box
modalOverlay.addEventListener("click", (event) => {
  if (event.target === modalOverlay) {
    modalOverlay.style.display = "none";
  }
});
