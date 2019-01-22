import { handleActions } from 'redux-actions';
import {
  openRequestPopup,
  closeRequestPopup,
  selectRequestType
} from '../actions/requestActions';

export const initialState = {
  openPopup: false,
  data: {}
};

const actionHandlers = {
  [openRequestPopup]: (state, action) => ({ ...state, openPopup: true }),
  [closeRequestPopup]: (state, action) => ({ ...state, openPopup: false }),
  [selectRequestType]: (state, action) => ({
    ...state,
    requestType: action.payload
  })
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
