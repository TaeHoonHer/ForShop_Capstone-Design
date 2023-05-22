import Main from '../Pages/Main';
import IntroLoad from '../Pages/IntroLoad';
import Best from '../Pages/Best';
import Login from '../Pages/Login';
import Upload from '../Pages/Upload';
import UpImage from '../Pages/UpImage';
import UpVideo from '../Pages/UpVideo';
import AnVideo from '../Pages/AnVideo';
import MainVideo from '../Pages/MainVideo';

const routes = [
    {
        path: '/',
        component: IntroLoad
    },
    {
        path: '/main',
        component: Main
    },
    {
        path: '/best',
        component: Best
    },
    {
        path: '/login',
        component: Login
    },
    {
        path: '/upload',
        component: Upload
    },
    {
        path: '/upimg',
        component: UpImage
    },
    {
        path: '/upvideo',
        component: UpVideo
    },
    {
        path: '/anvideo',
        component: AnVideo
    },
    {
        path: '/main-video',
        component: MainVideo
    },
];

export default routes;






