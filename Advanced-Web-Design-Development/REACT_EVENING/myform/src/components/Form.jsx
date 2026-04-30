function Form() {
  return (
    <div className="formContainer">
      <h2>LOGIN FORM</h2>
      <div className="formGroup">
        <label>USERNAME:</label>
        {/* <br /> */}
        <input type="text" name="username" />
      </div>
      <div className="formGroup">
        <label>PASSWORD:</label>
        {/* <br /> */}
        <input type="password" name="password" />
      </div>
      <div className="formGroup">
        <input type="submit" name="insert" value={"SIGN IN"} />
      </div>

      <div>
        <img src="avatar.jpg" alt="Avatar" />
      </div>
    </div>
  );
}

export default Form;
