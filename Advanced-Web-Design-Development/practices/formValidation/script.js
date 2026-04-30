const btn = document.getElementById("btn");

btn.addEventListener("click", function (event) {
  let username = document.getElementById("username").value;
  let password = document.getElementById("password").value;

  if (username === "" || password === "") {
    alert("Please fill in all fields.");
    event.preventDefault(); // ✅ stop submission only when invalid
    return;
  }

  alert("Form submitted successfully!");
  //   event.preventDefault(); // ✅ stop submission for demonstration
});
