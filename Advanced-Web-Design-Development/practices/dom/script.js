// element = document.getElementById("dom");
const element = document.querySelector("#dom");

element.innerHTML = "THIS IS DOM MANIPULATION";
element.style.color = "blue";
element.style.backgroundColor = "yellow";
element.style.padding = "20px";
element.style.fontSize = "24px";
element.style.fontWeight = "bold";
element.style.textAlign = "center";
element.style.marginTop = "50px";

const button = document.getElementById("btn");
button.innerHTML = "LOGIN";
button.style.padding = "10px 20px";
button.style.margin = "20px 45%";
button.style.fontSize = "18px";
button.style.backgroundColor = "green";
button.style.color = "white";
button.style.border = "none";
button.style.borderRadius = "5px";
button.style.cursor = "pointer";
button.addEventListener("click", function () {
  alert("Button Clicked!");
  button.innerHTML = "Clicked Me!";
  button.style.backgroundColor = "red";
});

document.body.style.overflow = "hidden";

const deleteButton = document.querySelector(".del");
deleteButton.style.padding = "10px 20px";
deleteButton.style.margin = "20px 45%";
deleteButton.style.fontSize = "18px";
deleteButton.style.backgroundColor = "red";
deleteButton.style.color = "white";
deleteButton.style.border = "none";
deleteButton.style.borderRadius = "5px";
deleteButton.style.cursor = "pointer";
deleteButton.addEventListener("click", function () {
  element.remove();
  deleteButton.remove();
});
