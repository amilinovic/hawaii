import React from 'react';
import ReactDOM from 'react-dom';
import 'reset-css/reset.css';
import App from './App';
import registerServiceWorker from './registerServiceWorker';
import './styles/globalStyles.js';
// import 'bootstrap/dist/css/bootstrap.min.css';

ReactDOM.render(<App />, document.getElementById('root'));
registerServiceWorker();
