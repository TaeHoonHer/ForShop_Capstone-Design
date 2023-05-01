import './App.css';
import React from 'react';
import { BrowserRouter as Router, Routes, Link } from 'react-router-dom';
import Main from './Pages/Main';

function App() {
  return (
    <div className="App">
      <div className="app_box">
        <header>
          <p>For Shop</p>
        </header>
        
        <Router>
          <div className='to_main'>
            <Link to="/main"><p>Let's Start!</p></Link>
          </div>
          <Routes path="/main" component={Main}/>
        </Router>
        
        <div className="intro_container">
          <div className="img_container">
            <span className='bg2' style={{'--i' : '1'}}><img src="/img/mountain.jpg"/></span>
            <span className='bg3' style={{'--i' : '2'}}><img src="/img/bg2.png"/></span>
            <span className='bg4' style={{'--i' : '3'}}><img src="/img/bg3.png"/></span>
            <span className='bg5' style={{'--i' : '4'}}><img src="/img/bg4.png"/></span>
            <span className='bg6' style={{'--i' : '5'}}><img src="/img/bg5.png"/></span>
            <span className='bg7' style={{'--i' : '6'}}><img src="/img/bg6.png"/></span>
            <span className='bg8' style={{'--i' : '7'}}><img src="/img/bg7.png"/></span>
            <span className='bg9' style={{'--i' : '8'}}><img src="/img/bg8.png"/></span>
            <p className="intro_text">Best 8<br></br>of <br/>For Shop</p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
