import React from 'react';
import '../Css/Footer.css';
import styled from 'styled-components';

const FooterWrapper = styled.div`
    width : 100%;
    height : 10vh;
    display : flex;
    justify-content : center;
    align-items : center;
    margin : 0;
    padding : 0;
    background : #f386fd;
    color : #fff;
    overflow-x : hidden;
    letter-spacing : 2px;
    font-size : 15px;
`;

function Footer() {
    return (
        <FooterWrapper>
            <div className='FooterContainer'>
                <p>ⓒ 2023 ForShop Team. &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;All Rights Reserved.</p>
            </div>
        </FooterWrapper>
    );
}

export default Footer;