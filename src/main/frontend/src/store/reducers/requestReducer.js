import { handleActions } from 'redux-actions';
import { openRequestPopup, closeRequestPopup } from '../actions/requestActions';

export const initialState = {
  openPopup: false,
  data: {}
};

const actionHandlers = {
  [openRequestPopup]: (state, action) => ({ ...state, openPopup: true }),
  [closeRequestPopup]: (state, action) => ({ ...state, openPopup: false })
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
