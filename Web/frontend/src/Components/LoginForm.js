import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../Css/LoginForm.css';

function LoginForm() {
  const squareArr = Array.from({ length: 5 }, (_, i) => i);
  const colorArr = Array.from({ length: 3 }, (_, i) => i);

  const [userPassword, setUserPassword] = useState('');
  const [passwordCheck, setPasswordCheck] = useState('');

  const [nickname, setNickname] = useState('');
  const [userId, setUserId] = useState('');
  const [email, setEmail] = useState('');

  const navigate = useNavigate();

  const handleFormLogin = async (event) => {
    event.preventDefault();

    const lgData = {
      userId,
      userPassword
    };
  
    try {
      const response = await axios.post('/api/auth/login', lgData);

      const accessToken = response.data.accessToken;
      const refreshToken = response.data.refreshToken;
  
      if (response.data.status === 200) {
        navigate('/main');
      } else { 
        navigate('/login');
        alert('로그인 오류')
      }
      
      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('refreshToken', refreshToken);
    } catch (error) {
      if (error.response && error.response.status === 401) {
        alert('입력하신 비밀번호 정보가 일치하지 않습니다.');
      } else {
        console.log(error);
      }
    }
  }

  const handleFormSubmit = async () => {
    const user = {
      nickname,
      userId,
      userPassword,
      email
    };

    try {
      const response = await axios.post('/api/auth/signup', user).then(
        // 회원가입 요청이 성공했으므로 사이드바를 닫습니다.
        setSidebarOpen(false),

        // 페이지를 /login으로 이동시킵니다.
        navigate('/login')
      );
    } catch (error) {
      console.log(error);
    }
  };

  // sidebar 열림 상태 추적
  const [isSidebarOpen, setSidebarOpen] = useState(false);

  const handleSidebarToggle = () => {
    setSidebarOpen(!isSidebarOpen);
  };

  const redirectUrl = (event) => {
    event.preventDefault();
    window.location.href = 'http://192.168.0.11:8080/oauth2/authorization/kakao';
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
            <input 
              type="text" 
              placeholder="NickName" 
              value={nickname}
              onChange={(e) => setNickname(e.target.value)} 
            />
          </li>
          <li>
              <input 
                type="text" 
                placeholder="UserID"
                value={userId}
                onChange={(e) => setUserId(e.target.value)}
              />
              {(userId.length < 7 || userId.length > 25) && (
                <p class="error-message" style={{ color: 'red', fontSize: '10px' }}>
                  아이디는 7~25자입니다. 다시 입력해주세요.
                </p>
              )}
          </li>
          <li>
            <input
              type="password"
              placeholder="Password"
              value={userPassword}
              onChange={(e) => setUserPassword(e.target.value)}
            />
            {(userPassword.length < 8 || userPassword.length > 30) && (
                <p class="error-message" style={{ color: 'red', fontSize: '10px' }}>
                  비밀번호는 8~30자 입니다. 다시 입력해주세요.
                </p>
              )}
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
              {userPassword && passwordCheck && userPassword !== passwordCheck && (
                <p class="error-message" style={{ color: 'red', fontSize: '10px' }}>
                  입력하신 정보와 일치하지 않습니다. 다시 입력해주세요.
                </p>
              )}
            </div>
          </li>
          <li>
            <input 
              type="email" 
              placeholder="EmailAddress"
              value={email}
              onChange={(e) => setEmail(e.target.value)} 
            />
          </li>
          <li>
            <button className='suCheck' type="button" onClick={handleFormSubmit}>확인</button>
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
                  <input 
                  type="text" 
                  placeholder="UserID" 
                  value={userId}
                  onChange={(e) => setUserId(e.target.value)}
                  />
                </div>
                <div className="inputBox">
                  <input
                    type="password"
                    placeholder="Password"
                    value={userPassword}
                    onChange={(e) => setUserPassword(e.target.value)}
                  />
                </div>
                <div className="inputBox">
                  <div className="submitContainer">
                    <input type="submit" value="Login" onClick={handleFormLogin}/>
                  </div>
                  <div className="kakaoButtonContainer">
                    <button className="kakaoButton" onClick={redirectUrl}>
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