import React from 'react';
import styled from 'styled-components';
import '../Css/UpImgForm.css';

const FormBox = styled.form`
    width: 100%;
    height: calc(100% - 50px);
    margin: 0;
    padding: 0;
    display: flex;
    justify-content: center;
    align-items: center;
`;

const ImageBox = styled.div`
    width: 50%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    border : 2px solid #f386fd;
    border-style : dotted;
`;

const Image = styled.div`
    text-align: center;
    img {
        width: 50%;
    }
`;

const MainContents = styled.div`
    width: 50%;
    height: 100%;
`;

const InTitle = styled.div`
    input {
        width: 100%;
        height: 50px;
        border: 1px solid #ccc;
        padding: 10px;
        font-size: 16px;
    }
`;

const InKeyword = styled.div`
    input {
        width: 100%;
        height: 50px;
        border: 1px solid #ccc;
        padding: 10px;
        font-size: 16px;
    }
`;

const InContents = styled.div`
    input {
        width: 100%;
        height: 200px;
        border: 1px solid #ccc;
        padding: 10px;
        font-size: 16px;
    }
`;

const FormContents = styled.div`
    width: 100%;
    height: 80%;
    display: flex;
`;

function UpImgForm () {
    return (
        <FormBox>
            <FormContents>
                <ImageBox>
                    <div className='ImgBox'>
                        <Image>
                            <img src = "/img/gallery.png"/>
                            <h2>이미지를 여기에 드래그 하세요.</h2>
                            <label htmlFor="file-upload" className="custom-file-upload">
                                <input id="file-upload" type='file' />
                            </label>
                        </Image>
                    </div>
                </ImageBox>
                <MainContents>
                    <div className='title'>
                        <InTitle>
                            <input type='text' className='ttarea' placeholder='제목 입력'/>
                        </InTitle>
                    </div>
                    <div className='keyword'>
                        <InKeyword>
                            <input type="text" className='karea' placeholder='키워드 입력'/>
                        </InKeyword>
                    </div>
                    <div className='contents'>
                        <InContents>
                            <input type = "text" className='ctarea' placeholder='내용 입력'/>
                        </InContents>
                    </div>
                </MainContents>
            </FormContents>
        </FormBox>
    )
}

export default UpImgForm;