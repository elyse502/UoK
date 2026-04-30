const nationality = prompt("What is your nationality?").toLowerCase();

if (nationality === "rwandan") {
  console.log("You are eligible to vote.");
  alert("You are eligible to vote.");
  document.write("You are eligible to vote 🇷🇼.");
} else {
  console.log("You are not eligible to vote.");
  alert("You are not eligible to vote.");
  document.write("You are not eligible to vote.");
}
