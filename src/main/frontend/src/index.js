import React from 'react';
import ReactDOM from 'react-dom';
import '../node_modules/reset-css/reset.css';
import App from './App';
import registerServiceWorker from './registerServiceWorker';
import { injectGlobal } from 'styled-components';
import 'bootstrap/dist/css/bootstrap.min.css';

ReactDOM.render(<App />, document.getElementById('root'));
registerServiceWorker();

injectGlobal`
    a {
        text-decoration: none;
    }
`;
