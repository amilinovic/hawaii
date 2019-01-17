import { handleActions } from 'redux-actions';
import {
  openRequestPopup,
  closeRequestPopup
} from '../actions/requestPopupAction';

export const initialState = false;

const actionHandlers = {
  [openRequestPopup]: () => true,
  [closeRequestPopup]: () => false
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
