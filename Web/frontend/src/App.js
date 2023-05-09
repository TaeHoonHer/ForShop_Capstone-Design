import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import routes from './router/routes';

function App() {
    return (
      <Router>
        <Routes>
          {routes.map((route) => (
            <Route key={route.name} path={route.path} element={<route.component />} />
          ))}
        </Routes>
      </Router>
    );
}

export default App;
