import React from 'react';
import ReactDOM from 'react-dom';
import 'reset-css/reset.css';
import App from './App';
import registerServiceWorker from './registerServiceWorker';
import 'bootstrap/dist/css/bootstrap.min.css';
import BaseStyles from './styles/BaseStyles';

BaseStyles();
const rootEl = document.getElementById('root');

let render = () => {
  ReactDOM.render(<App />, rootEl);
};

if (module.hot) {
  module.hot.accept('./App', () => {
    setTimeout(render);
  });
}

render();
registerServiceWorker();
