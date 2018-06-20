import { RECEIVE_API_DATA } from '../actions/types';

export default (state = { fetching: true }, { type, data }) => {
  switch (type) {
    case RECEIVE_API_DATA:
      console.log(data);
      return { fetching: false, data };
    default:
      return state;
  }
};
