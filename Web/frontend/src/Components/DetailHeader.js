import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';
import '../Css/DetailHeader.css';
import axios from 'axios';

const UPLink = styled(Link)`
  display: flex;
  img {
    margin-right : 8px;
    margin-top : 5px;
  }
`;

const DetailHeaderWrapper = styled.div`
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

function DetailHeader() {
  const [userId, setUserId] = useState(null);

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const accessToken = localStorage.getItem('accessToken');
        
        const response = await axios.get('/api/auth/user', {
          headers: {
            'Authorization': `Bearer ${accessToken}`
          }
        });

        if (response.status === 200) {
          setUserId(response.data.userId);
        }
      } catch (error) {
        console.error(error);
      }
    };

    fetchUserData();
  }, []);

  const handleClick = (event) => {
    if (userId) {
      event.preventDefault();
    }
  }

  return (
    <DetailHeaderWrapper id='detail-header'>
      <Logo>
        <img src='/img/Logo.png' alt='ForShop Logo' />
        <Link to='/IntroimgMain'>
          <p>ForShop</p>
        </Link>
      </Logo>
      <MenuList>
        <MenuItem>
          <li>
            <UPLink to='/mypage'>
              <img id='upicon' src='/img/user.png' alt='upload icon' style={{ width: '20px', height: '20px' }}/>
              <p className='nav-link2' style={{ color: '#fd86fd' }}>MyPage</p>
            </UPLink>
          </li>
        </MenuItem>
        <MenuItem>
          <li>
            <Link to='/main'>
              <p className='nav-link2' style={{ color: '#fd86fd' }}>
                Home
              </p>
            </Link>
          </li>
        </MenuItem>
        <MenuItem>
          <li>
            <Link to='/login' onClick={handleClick}>
            <p className='nav-link2' style={{ color: '#fd86fd' }}>
                {userId ? `${userId}님` : 'Login'}
              </p>
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
    </DetailHeaderWrapper>
  );
}

export default DetailHeader;