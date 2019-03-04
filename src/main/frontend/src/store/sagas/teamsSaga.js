import { call, put, takeLatest } from 'redux-saga/effects';
import {
  errorReceivingTeams,
  receiveTeams,
  requestTeams
} from '../actions/teamsActions';
import { getTeamsApi } from '../services/teamsService';
import {
  errorHandlingAction,
  withErrorHandling
} from './HOC/withErrorHandling';

const getAllTeamsSaga = function*() {
  const teamsInformation = yield call(getTeamsApi);
  yield put(receiveTeams(teamsInformation));
};

export const teamsSaga = [
  takeLatest(
    requestTeams,
    withErrorHandling(getAllTeamsSaga, () =>
      errorHandlingAction(errorReceivingTeams)
    )
  )
];
