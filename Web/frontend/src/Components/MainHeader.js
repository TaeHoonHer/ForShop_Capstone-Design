import React, { useState } from 'react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';
import '../Css/MainHeader.css';

const DropdownMenu = styled.ul`
  position: absolute;
  top: 100%;
  left: 0;
  p {
    font-size : 20px;
    color : #f386fd;
  }
  display: ${(props) => (props.isOpen ? 'block' : 'none')};
`;

const UPLink = styled(Link)`
  display: flex;
  img {
    margin-right : 8px;
    margin-top : 5px;
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
  margin-left: 40px;
  font-size: 20px;
  cursor: pointer;
  color: #fd86fd;
`;

function MainHeader() {
  const [isOpen, setIsOpen] = useState(false);

  const handleDownClick = () => {
    setIsOpen(!isOpen);
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
          <DownLink onClick={handleDownClick}>
            <p className='nav-link2'>Images</p>
            <img id='down' src='/img/down.png' alt='dropdown icon' style={{ width: '20px', height: '20px' }} />
          </DownLink>
          <DropdownMenu isOpen={isOpen}>
            <li>
              <Link to='/main'>
                <p className='nav-link2'>Videos</p>
              </Link>
            </li>
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