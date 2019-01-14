import { createStore, applyMiddleware, compose } from 'redux';
import createSagaMiddleware from 'redux-saga';
import reducer from './reducers/reducers';
import saga from './sagas/sagas';
import { createBrowserHistory } from 'history';
import { connectRouter, routerMiddleware } from 'connected-react-router';

export const history = createBrowserHistory();

const sagaMiddleware = createSagaMiddleware();
const reactRouterMiddleware = routerMiddleware(history);

var compose_args = [applyMiddleware(sagaMiddleware, reactRouterMiddleware)];
if (process.env.NODE_ENV === 'development')
  if (window.__REDUX_DEVTOOLS_EXTENSION__)
    compose_args.push(window.__REDUX_DEVTOOLS_EXTENSION__());

export default createStore(
  connectRouter(history)(reducer),
  compose.apply(this, compose_args)
);

sagaMiddleware.run(saga);
