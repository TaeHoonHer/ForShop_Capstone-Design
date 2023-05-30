import React, { useState, useCallback, useEffect } from 'react';
import styled from 'styled-components';
import '../Css/UpImgForm.css';
import axios from 'axios';

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
  position: relative;
`;

const Image = styled.div`
  width: 50%;
  height: 50%;
  object-fit: contain;

  img {
    width: 100%;
    height: 100%;
    object-fit: contain;
  }

  video {
    width : 100%;
    height : 100%;
    object-fit: contain;
  }
`;

const UploadPrompt = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  width: 50%;
  height: 50%;
  h2 {
    margin-top : 20px;
    font-size : 1rem;
  }
`;

const MainContents = styled.div`
  p {
    font-size : 25px;
  }
  width: 50%;
  height: 100%;
  .ContentsBox {
    width: 100%;
    height: 100%;
    padding: 0;
    margin: 0;
  }
`;

const InTitle = styled.div`
  input {
    width: 85%;
    height: 50px;
    border: none;
    padding: 15px;
    font-size: 16px;
    background: rgba(255, 255, 255, 0.2);
    outline : none;
    border-radius: 14px;
    box-shadow: 0 25px 45px rgba(0, 0, 0, 0.1);
    border : none;
    border-right : 1px solid rgba(255, 255, 255, 0.2);
    border-bottom : 1px solid rgba(255, 255, 255, 0.2);
    letter-spacing: 1px;
    color : #333;
  }
`;

const InKeyword = styled.div`
  margin-top : 3%;
  input {
    width: 85%;
    height: 50px;
    padding: 15px;
    font-size: 16px;
    background: rgba(255, 255, 255, 0.2);
    outline : none;
    border-radius: 14px;
    box-shadow: 0 25px 45px rgba(0, 0, 0, 0.1);
    border : none;
    border-right : 1px solid rgba(255, 255, 255, 0.2);
    border-bottom : 1px solid rgba(255, 255, 255, 0.2);
    letter-spacing: 1px;
    color : #333;
  }
`;

const InContents = styled.div`
  margin-top : 3%;
  input {
    width: 85%;
    height: calc(100%-100px);
    padding-top: 10px;
    padding-bottom: 50px;
    padding-left : 15px;
    font-size: 16px;
    background: rgba(255, 255, 255, 0.2);
    outline : none;
    border-radius: 20px;
    box-shadow: 0 25px 45px rgba(0, 0, 0, 0.1);
    border : none;
    border-right : 1px solid rgba(255, 255, 255, 0.2);
    border-bottom : 1px solid rgba(255, 255, 255, 0.2);
    letter-spacing: 1px;
    color : #333;
  }
`;

const FormContents = styled.div`
  width: 100%;
  height: 80%;
  display: flex;
`;

const CheckBtn = styled.button`
  width: 80px;
  height: 50px;
  margin : 0;
  background: #f386fd;
  backdrop-filter: blur(20px);
  border-radius : 20px;
  color: white;
  border: none;
  cursor : pointer;
  button {
    background: transparent;
    font-size: 15px;
    border : none;
    font-size : 20px;
    color : white;
    cursor : pointer;
  }
`;

function UpImgForm(props) {
    const { video: videoProp, onFormSubmit } = props;
    const [mediaFile, setMediaFile] = useState(null);
    const [fileObject, setFileObject] = useState(null);
    const [isVideo, setIsVideo] = useState(false);
    const [title, setTitle] = useState("");
    const [hashtag, setHashtag] = useState("");
    const [contents, setContents] = useState("");
  
    useEffect(() => {
      if (videoProp) {  // Use renamed prop here
        setMediaFile(videoProp);
        setIsVideo(true);
      }
    }, [videoProp]);

    const handleTitleChange = (event) => {
      setTitle(event.target.value);
    };

    const handleHashtagChange = (event) => {
      setHashtag(event.target.value);
    };

    const handleContentsChange = (event) => {
      setContents(event.target.value);
    };

    const handleUpload = async (e) => {
      e.preventDefault();
      let content = contents + hashtag;
    
      // Check if the fileObject is not null
      if (fileObject) {
        try {
          const formDataToUpload = new FormData();
          formDataToUpload.append('title', title);
          formDataToUpload.append('content', content);
          formDataToUpload.append('multipartFile', fileObject);
  
          const accessToken = localStorage.getItem('accessToken');
          console.log(accessToken);
  
          const response = await axios.post("/api/articles", formDataToUpload, {
            headers: {
              'Authorization': `Bearer ${accessToken}`,
              'Content-Type': 'multipart/form-data',
            }
            
          });
  
          onFormSubmit(response); // call the callback here
        } catch (err) {
          console.error(err);
        }
      } else {
        console.log('No file selected');
      }
    
      e.stopPropagation();
    };
  
    const handleFileChange = (event) => {
      const file = event.target.files[0];
      handleFileUpload(file);
    };

    const handleFileUpload = useCallback((file) => {
      if (file) {
          setFileObject(file); // add this line
          const reader = new FileReader();
          reader.readAsDataURL(file);
          reader.onloadend = () => {
          setMediaFile(reader.result);
          setIsVideo(file.type.startsWith('video'));
          };
      } else {
          alert('No file chosen.');
      }
    }, []);

    const handleDrop = useCallback((event) => {
      event.preventDefault();
      const file = event.dataTransfer.files[0];
      handleFileUpload(file);
    }, [handleFileUpload]);

    const handleDragOver = (event) => {
      event.preventDefault();
    };

    return (
      <FormBox onDrop={handleDrop} onDragOver={handleDragOver} onSubmit={handleUpload}>
      <FormContents>
        <ImageBox>
          {mediaFile ? (
            <Image>
              {isVideo ? (
                <video src={mediaFile} autoPlay muted/>
              ) : (
                <img src={mediaFile} />
              )}
            </Image>
          ) : (
            <UploadPrompt>
              <Image>
                <img src="/img/gallery.png" />
              </Image>
              <h2>이미지를 여기에 드래그 하세요.</h2>
              <label htmlFor="file-upload" className="custom-file-upload">
                파일 선택
                <input id="file-upload" type="file" onChange={handleFileChange}/>
              </label>
            </UploadPrompt>
          )}
        </ImageBox>
            <MainContents>
                <div className="ContentsBox">
                  <div className="title">
                    <InTitle>
                      <p>Title</p>
                      <input type="text" className="ttarea" placeholder="제목 입력" value={title} onChange={handleTitleChange} />
                    </InTitle>
                    <InKeyword>
                      <p>HashTag</p>
                      <input type="text" className="karea" placeholder="키워드 입력" value={hashtag} onChange={handleHashtagChange} />
                    </InKeyword>
                    <InContents>
                      <p>Contents</p>
                      <input type="textarea" className="ctarea" placeholder="내용 입력" value={contents} onChange={handleContentsChange} />
                    </InContents>
                  </div>
                  <CheckBtn className="CheckBtn">
                    <button type='button' onClick={handleUpload} className='check'>Go!</button>
                  </CheckBtn>
                </div>
            </MainContents>
        </FormContents>
    </FormBox>
  );
}

export default UpImgForm;