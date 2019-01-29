import { call, put, takeLatest } from 'redux-saga/effects';
import { getUsersApi } from '../services/teamCalendarApi';
import moment from 'moment';
import { initTeamCalendar, requestUsers } from '../actions/teamCalendarActions';
import { receiveUsers } from '../actions/teamCalendarActions';

export const getUsers = function*() {
  try {
    const users = yield call(getUsersApi);
    yield put(receiveUsers(users));
    const now = moment();
    yield put(
      initTeamCalendar({
        /*now: now,
                months: moment.months(),
                year: now.year(),
                currentMonth: moment.months(now.month()),
                currentDay: moment().date(),
                myPersonalDays: myPersonalDays*/
      })
    );
  } catch (error) {
    //yield put(errorReceivingUsers(error));
  }
};

export const teamCalendarSaga = [takeLatest(requestUsers, getUsers)];
