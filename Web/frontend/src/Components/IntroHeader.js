import React from 'react';
import styled from 'styled-components';
import { Link, useLocation } from 'react-router-dom';

const HeaderWrapper = styled.div`
  position: absolute;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 40px;
  background: transparent;
  border: none;
  z-index : 2;
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

const MenuItem = styled.p`
  margin-left: 40px;
  font-size: 20px;
  cursor: pointer;
  color: #fd86fd;
  &:hover {
    color: black !important;
  }
`;

function Header() {
  const location = useLocation();
  return (
    <HeaderWrapper>
      <Logo>
        <img src="/img/Logo.png" alt="ForShop Logo"/>
        <Link to="/">
          <p>ForShop</p>
        </Link>
      </Logo>
      <MenuList>
        {location.pathname !== "/IntroimgMain" && (
          <MenuItem>
            <Link to="/IntroimgMain">
              <p className='nav-link' style={{ color: '#fd86fd' }}>Home</p>
            </Link>
          </MenuItem>
        )}
      </MenuList>
    </HeaderWrapper>
  );
}

export default Header;