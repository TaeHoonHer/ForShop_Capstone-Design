import React, { useState } from 'react';
import '../Css/Mypage.css';
import DetailHeader from '../Components/DetailHeader';
import Footer from '../Components/Footer';
import { Link } from 'react-router-dom';

function Mypage() {
    const [myinfos, setMyinfos] = useState([]);

    return(
        <div className='MypageWrapper'>
            <DetailHeader/>
            <div className='MypageBox'>
                <div className='Myinfo'>
                    <div className='Myimg'>
                        <img src = "/img/user.png"/>
                    </div>
                    <div className='Myresume'>
                        <h2 className='nickname'>user1</h2>
                        <h3 className='email'>user@naver.com</h3>
                    </div>
                </div>
                <div className='Mycontents'>
                    <h2 className='MyctTitle'>게시물</h2>
                    <div className='Myoptions'>
                        <button type='button' className='delete'>삭제</button>
                        <button type='button' className='settings'>수정</button>
                    </div>
                    <div className='Myimgcontainer'>
                        {myinfos.map((info, index) => {
                            <div key = {index}>
                                <Link to={{
                                  pathname: `/main/imgboard`,
                                }}>
                                  <img 
                                    src={"https://forshop-bucket.s3.ap-northeast-2.amazonaws.com/" + info.storedName}
                                    key = {info.id}
                                    className='imageList'
                                  />
                                </Link>
                            </div>
                        })}
                    </div>
                </div>
            </div>
            <Footer/>
        </div>
    );
}

export default Mypage;