import React, { useState } from 'react';
import '../Css/LoginForm.css';

function LoginForm() {
  const squareArr = Array.from({ length: 5 }, (_, i) => i);
  const colorArr = Array.from({ length: 3 }, (_, i) => i);

  // sidebar 열림 상태 추적
  const [isSidebarOpen, setSidebarOpen] = useState(false);

  const handleSidebarToggle = () => {
    setSidebarOpen(!isSidebarOpen);
  }

  return (
    <div className="LoginForm">
      {isSidebarOpen && (
        <div
          className="overlay"
          style={{
            position: 'fixed',
            top: 0,
            left: 0,
            width: '100%',
            height: '100%',
            backgroundColor: 'rgba(0, 0, 0, 0.5)',
            zIndex: 1,
          }}
          onClick={handleSidebarToggle}
        />
      )}
      <div className="sidebar" style={{ right: isSidebarOpen ? '0' : '-100%' }}>
        <ul className="menu">SignUp
            <li><input type="text" placeholder="Username"/></li>
            <li><input type="text" placeholder="UserID"/></li>
            <li><input type="password" placeholder="Password"/></li>
            <li><input type="email" placeholder="EmailAddress"/></li>
            <li><input type="submit" value="확인"/></li>
        </ul>
      </div>
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
                  <div className="submitContainer">
                    <input type="submit" value="Login" />
                  </div>
                  <div className="kakaoButtonContainer">
                    <button className="kakaoButton">
                      <img src="/img/kakao_login.png" alt="Kakao Login" />
                    </button>
                  </div>
                </div>
                <p className="forget">Forgot Password ? <button type='button' onClick='' className='find'>Click Here</button></p>
                <p className="forget">Don't have an account ? <button type='button' onClick={handleSidebarToggle} className='signup'>Sign up</button></p>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default LoginForm;