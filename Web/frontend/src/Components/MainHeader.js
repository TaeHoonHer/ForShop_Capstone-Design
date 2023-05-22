import React, { useState } from 'react';
import styled from 'styled-components';
import { Link, useNavigate } from 'react-router-dom';
import '../Css/MainHeader.css';

const UPLink = styled(Link)`
  display: flex;
  img {
    margin-right : 8px;
    margin-top : 5px;
  }
`;

const DropdownMenu = styled.ul`
  position: absolute;
  list-style-type : none;
  top: 100%;
  left: 0;
  opacity: ${(props) => (props.isOpen ? '1' : '0')};
  transform: ${(props) => (props.isOpen ? 'translateY(0)' : 'translateY(-10px)')};
  transition: opacity 0.3s ease, transform 0.3s ease;
  pointer-events: ${(props) => (props.isOpen ? 'auto' : 'none')};
  p {
    font-size : 20px;
    color : #f386fd;
  }
`;

const DownLink = styled.button`
  display: flex;
  align-items: center;
  background: none;
  border: none;
  cursor: pointer;
  color : #f386fd;
  font-size : 20px;
`;

const MainHeaderWrapper = styled.div`
  position : abolsolute;
  top : 0;
  left : 0;
  margin : 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 40px;
  background : #fff;
  border : none;
`;

const Logo = styled.div`
  display: flex;
  align-items: center;
  img {
    width: 30px;
    height: 30px;
  }
  p {
    margin: 0;
    font-weight: bold;
    font-size: 25px;
    margin-left: 15px;
    color : #f386fd;
  }
`;

const MenuList = styled.ul`
  display: flex;
  align-items: center;
  justify-content: flex-end;
  list-style: none;
  margin: 0;
  padding: 0;
`;

const MenuItem = styled.li`
  position : relative;
  margin-left: 40px;
  font-size: 20px;
  cursor: pointer;
  color: #fd86fd;
`;

function MainHeader() {
  const [isOpen, setIsOpen] = useState(false);
  const [menuName, setMenuName] = useState('Images');
  const navigate = useNavigate();

  const handleDownClick = (e) => {
    e.stopPropagation();
    setIsOpen(!isOpen);
  };

  const handleMenuClick = (name) => {
    setMenuName(name);
    setIsOpen(false);
    if(name === 'Images'){
      navigate('/main');
    } else {
      navigate('/main-video');
    }
  };

  return (
    <MainHeaderWrapper id='main-header'>
      <Logo>
        <img src='/img/Logo.png' alt='ForShop Logo' />
        <Link to='/'>
          <p>ForShop</p>
        </Link>
      </Logo>
      <MenuList>
      <MenuItem>
      <DownLink>
        <Link to={menuName === 'Images' ? '/main' : '/main-video'}>
          <p className='nav-link2'>{menuName}</p>
        </Link>
        <img id='down' src='/img/down.png' alt='dropdown icon' style={{ width: '20px', height: '20px' }} onClick={handleDownClick} />
      </DownLink>
      <DropdownMenu isOpen={isOpen}>
        {menuName === 'Images' ? (
          <div onClick={() => handleMenuClick('Videos')}>
            <li>
              <p className='nav-link2'>Videos</p>
            </li>
          </div>
        ) : (
          <div onClick={() => handleMenuClick('Images')}>
            <li>
              <p className='nav-link2'>Images</p>
            </li>
          </div>
        )}
      </DropdownMenu>
    </MenuItem>
        <MenuItem>
          <li>
            <Link to='/login'>
              <p className='nav-link2' style={{ color: '#fd86fd' }}>Login</p>
            </Link>
          </li>
        </MenuItem>
        <MenuItem>
          <li>
            <UPLink to='/upload'>
              <img id='upicon' src='/img/upload.png' alt='upload icon' style={{ width: '20px', height: '20px' }}/>
              <p className='nav-link2' style={{ color: '#fd86fd' }}>Upload</p>
            </UPLink>
          </li>
        </MenuItem>
      </MenuList>
    </MainHeaderWrapper>
  );
}

export default MainHeader;