import React, { useState, useEffect } from 'react';
import '../Css/Mypage.css';
import DetailHeader from '../Components/DetailHeader';
import Footer from '../Components/Footer';
import { Link } from 'react-router-dom';
import axios from 'axios';

function Mypage() {
    const [myinfos, setMyinfos] = useState([]);
    const [selectedImages, setSelectedImages] = useState([]);

    useEffect(() => {
        const accessToken = localStorage.getItem('accessToken');

        axios.get('/api/articles/mypage', {
            headers: {
                Authorization: `Bearer ${accessToken}`,
            },
        }).then((response) => {
            setMyinfos(response.data);
        })
        .catch(error => {
            console.error(error);
        });
    }, []);

    const handleImageSelect = (imageId) => {
        setSelectedImages(prevSelectedImages =>
            prevSelectedImages.includes(imageId)
                ? prevSelectedImages.filter(id => id !== imageId)
                : [...prevSelectedImages, imageId]
        );
    };

    const handleDelete = () => {
        selectedImages.forEach(imageId => {
            axios.delete('/api/articles/' + imageId, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
                },
            }).then(response => {
                setMyinfos(myinfos.filter(info => info.id !== imageId));
            }).catch(error => {
                console.error(error);
            });
        });
        setSelectedImages([]);
    };

    return(
        <div className='MypageWrapper'>
            <DetailHeader/>
            <div className='MypageContainer'>
                <div className='MypageBox'>
                    <div className='Myinfo'>
                        <div className='Myimg'>
                            <img src="/img/user.png"/>
                        </div>
                        <div className='Myresume'>
                            <h2 className='nickname'>{myinfos[0] && myinfos[0].nickname}</h2>
                            <h3 className='email'>{myinfos[0] && myinfos[0].email}</h3>
                        </div>
                    </div>
                    <div className='Mycontents'>
                        <h2 className='MytTitle'>게시물</h2>
                        <div className='Myoptions'>
                            <button type='button' className='delete' onClick={handleDelete}>삭제</button>
                        </div>
                        <hr/>
                        <div className='Myimgcontainer'>
                        {myinfos && myinfos.map((info, index) => {
                            return (
                              <div key = {index}>
                                <Link to={{
                                  pathname: `/main/imgboard/${info.id}`
                                }}>
                                  <img 
                                    src={"https://forshop-bucket.s3.ap-northeast-2.amazonaws.com/" + info.storedName}
                                    key = {info.id}
                                    className='imageList'
                                  />
                                </Link>
                                <input 
                                  type="checkbox" 
                                  onChange={() => handleImageSelect(info.id)}
                                />
                              </div>
                            )
                        })}
                        </div>
                    </div>
                </div>
            </div>
            <Footer/>
        </div>
    );
}

export default Mypage;
