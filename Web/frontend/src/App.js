import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import routes from './router/routes';
import { Provider } from 'react-redux';
import { store } from './store.js';

function App() {
    return (
      <Provider store={store}>
        <Router>
          <Routes>
            {routes.map((route) => (
              <Route key={route.name} path={route.path} element={<route.component />} />
            ))}
          </Routes>
        </Router>
      </Provider>
    );
}

export default App;
