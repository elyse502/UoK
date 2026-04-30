function Button() {
  function handleClick() {
    alert("You clicked the button!");
  }
  return (
    <div>
      <button className="btn" onClick={handleClick}>
        Click here!
      </button>
    </div>
  );
}

export default Button;
