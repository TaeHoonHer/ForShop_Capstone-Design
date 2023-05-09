import '../Css/Login.css';

function LoginForm() {
  const squareArr = Array.from({ length: 5 }, (_, i) => i);
  const colorArr = Array.from({ length: 3 }, (_, i) => i);

  return (
    <div className="LoginForm">
      <div className='LoginBox'>
        {colorArr.map((i) => (
          <div key={i} className="color"></div>
        ))}
        <div className="box">
          {squareArr.map((i) => (
            <div key={i} className="square" style={{ '--i': i }}></div>
          ))}
          <div className="container">
            <div className="form">
              <h2>Login Form</h2>
              <form>
                <div className="inputBox">
                  <input type="text" placeholder="Username" />
                </div>
                <div className="inputBox">
                  <input type="password" placeholder="Password" />
                </div>
                <div className="inputBox">
                  <input type="submit" value="Login" />
                </div>
                <p className="forget">Forgot Password ? <a href="#">Click Here</a></p>
                <p className="forget">Don't have an account ? <a href="#">Sign up</a></p>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default LoginForm;