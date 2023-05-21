import React, { useState } from 'react';
import axios from 'axios';
import '../Css/LoginForm.css';

function LoginForm() {
  const squareArr = Array.from({ length: 5 }, (_, i) => i);
  const colorArr = Array.from({ length: 3 }, (_, i) => i);

  const [password, setPassword] = useState('');
  const [passwordCheck, setPasswordCheck] = useState('');

  const [nickname, setNickname] = useState('');
  const [userID, setUserID] = useState('');
  const [emailAddress, setEmailAddress] = useState('');

  const handleFormSubmit = async () => {
    const user = {
      nickname,
      userID,
      password,
      emailAddress
    };

    try {
      const response = await axios.post('localhost:8080', user);
    } catch (error) {
      console.log(error);
    }
  };

  // sidebar 열림 상태 추적
  const [isSidebarOpen, setSidebarOpen] = useState(false);

  const handleSidebarToggle = () => {
    setSidebarOpen(!isSidebarOpen);
  };

  const handleDuplicateCheck = () => {
    // 중복 확인 로직 작성
  };

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
        <ul className="menu">
          SignUp
          <li>
            <input type="text" placeholder="NickName" />
          </li>
          <li>
            <div className="input-with-button">
              <input type="text" placeholder="UserID" />
              <button onClick={handleDuplicateCheck}>중복확인</button>
            </div>
          </li>
          <li>
            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </li>
          <li>
            <div style={{ width:'100%', position : 'relative', margin: '0', padding: '0' }}>
              <input
                type="password"
                placeholder="PasswordCheck"
                value={passwordCheck}
                onChange={(e) => setPasswordCheck(e.target.value)}
                className='input-full-width'
              />
              {password && passwordCheck && password !== passwordCheck && (
                <p class="error-message" style={{ color: 'red', fontSize: '10px' }}>
                  입력하신 정보와 일치하지 않습니다. 다시 입력해주세요.
                </p>
              )}
            </div>
          </li>
          <li>
            <input type="email" placeholder="EmailAddress" />
          </li>
          <li>
            <button type="button" onClick={
              () => {
                
              }
            }>확인</button>
          </li>
        </ul>
      </div>
      <div className="LoginBox">
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
                  <input type="text" placeholder="UserID" />
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
                <p className="forget">
                  Forgot Password ?{' '}
                  <button type="button" onClick="" className="find">
                    Click Here
                  </button>
                </p>
                <p className="forget">
                  Don't have an account ?{' '}
                  <button type="button" onClick={handleSidebarToggle} className="signup">
                    Sign up
                  </button>
                </p>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default LoginForm;