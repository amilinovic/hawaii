import React, { Component } from 'react';
import styled from 'styled-components';
import { bindActionCreators } from 'redux';
import {
  decrementMonth,
  incrementMonth,
  selectMonth
} from '../store/actions/teamCalendarActions';
import { requestUsers } from '../store/actions/teamCalendarActions';
import { connect } from 'react-redux';
import { getPublicHolidays, getUsers } from '../store/selectors';

import HolidayImg from '../img/holiday.svg';
import leaveImg from '../img/icons/leave_ss.png';
import bonusImg from '../img/icons/bonus_ss.png';
import sicknessImg from '../img/icons/sickness_ss.png';

import moment from 'moment';
import { requestPublicHolidays } from '../store/actions/publicHolidayActions';
import { ifProp, prop, switchProp } from 'styled-tools';

const leaveTypes = {
  DEDUCTED_LEAVE: leaveImg,
  BONUS_DAYS: bonusImg,
  SICKNESS: sicknessImg,
  NON_DEDUCTED_LEAVE: leaveImg
};

const weekDays = ['M', 'T', 'W', 'T', 'F', 'S', 'S'];

const TeamCalendarWrapper = styled.div``;

const TeamCalendarContainer = styled.div`
  display: flex;
  width: 100%;
`;

const TeamDaysTable = styled.table``;

const TeamTotalsTable = styled.table`
  background: lightblue;
  width: 225px;
`;

const TeamCalendarMenus = styled.div`
  background: yellow;
  width: 100%;
  height: 100px;
`;

const TeamUsersContainer = styled.div`
  width: 225px;
  padding-top: 35px;
`;

const TeamCalendarUser = styled.div`
  text-overflow: clip;
  width: 100%;
  height: 40px;
  margin-bottom: 10px;
  padding-left: 5px;
`;

const TeamCalendarUserImage = styled.div`
  width: 40px;
  height: 40px;
  background-image: url('https://lh4.googleusercontent.com/-C_koQKFWl2Y/AAAAAAAAAAI/AAAAAAAAAAc/apR_5I8dYng/s96-c/photo.jpg');
  background-size: 100%;
  margin-bottom: 10px;
  border-radius: 20px;
  display: inline-box;
`;

const TeamCalendarUserName = styled.div`
  font-size: 13px;
  vertical-align: top;
  margin-top: 13px;
  margin-left: 10px;
  display: inline-box;
  text-overflow: clip;
`;

const TeamCalendarUserTh = styled.th`
  width: 28px;
  text-align: center;
  vertical-align: middle;
  height: 35px;
  font-size:14px;
`;

const TeamCalendarUserTr = styled.tr``;

const TeamCalendarUserTd = styled.td`
  vertical-align: middle;
  text-align: center;
  font-size: 13px;
  height: 50px;
  padding-bottom: 10px;
`;
const TeamCalendarDay = styled.div`
  background: ${switchProp(prop('weekDay'), {
    S: 'lightgrey'
  })};
  background: ${switchProp(prop('requestStatus'), {
    APPROVED: 'lightgreen',
    PENDING: 'orange'
  })};
  height: 25px;
  vertical-align: middle;
  display: flex;
  align-items: center;
  justify-content: center;
  img {
    width: 15px;
  }
`;

const TeamCalendarTotalsTh = styled.th`
  font-size: 10px;
  border: 1px solid red;
  height: 35px;
  text-align: center;
  vertical-align: middle;
  width: 55px;
`;

const TeamCalendarTotalsTd = styled.td`
  font-size: 13px;
  height: 50px;
  vertical-align: middle;
  text-align: center;
  padding-bottom: 10px;
`;

const TeamCalendarViewButton = styled.button`
  margin: 5px;
  padding: 10px;
  padding-left: 10px;
  border-radius: 5px;
  background: red;
  color: white;
  padding-left: 30px;
`;

const TeamCalendarNavButton = styled.button`
  margin: 5px;
  padding: 10px;
  padding-left: 10px;
  border-radius: 5px;
  background: light-grey !important;
`;

const TeamCalendarNavSelect = styled.select`
  margin-left: -5px;
  margin-right: -5px;
`;

const TeamCalendarGridTd = styled.td`
  height: 150px;
  width: 160px;
  border: 1px solid red;
  &:first-child {
    width: 180px;
  }
`;

class TeamCalendar extends Component {
  currentMonth = (new Date()).getMonth();
  currentYear = (new Date()).getFullYear();

  componentDidMount() {
    this.props.requestUsers();
    this.props.requestPublicHolidays();
  }

  getUsers = async () => {
    if (!this.props.users) {
      await this.props.requestUsers();
      return this.props.users;
    }
    return this.props.users;
  };

  getPublicHolidays = async () => {
    if (
      !this.props.publicHolidays ||
      (this.props.publicHolidays && this.props.publicHolidays.length) < 1
    ) {
      await this.props.requestPublicHolidays();
      return this.props.publicHolidays;
    }
    return this.props.publicHolidays;
  };

  renderTeamUsers = () => {
    var users = [];

    this.props.users.forEach(user => {
      users.push(
        <TeamCalendarUser key={'TeamCalendarUser' + user.id}>
          <TeamCalendarUserImage key={'TeamCalendarUserImage' + user.id} />
          <TeamCalendarUserName key={'TeamCalendarUserName' + user.id}>
            {user.fullName}
          </TeamCalendarUserName>
        </TeamCalendarUser>
      );
    });

    return users;
  }

  renderDaysHeader = () => {
    var days = [];

    for (
      var i = 1;
      i <= new Date(this.currentYear, this.currentMonth, 0).getDate();
      i++
    ) {
      days.push(
        <TeamCalendarUserTh key={'TeamCalendarUserTh' + i}>
          {weekDays[new Date(this.currentYear, this.currentMonth, i).getDay()]}
        </TeamCalendarUserTh>
      );
    }
    return days;
  }

  renderDays = ()=> {
    var days = [];

    this.props.users.forEach(user => {
      var line = [];
      for (
        var j = 1;
        j <= new Date(this.currentYear, this.currentMonth, 0).getDate();
        j++
      ) {
        var personalDay = null;
        var publicHoliday = null;
        user.days.forEach(day => {
          if (
            day.date ===
            moment(new Date(this.currentYear, this.currentMonth - 1, j)).format(
              'YYYY-MM-DD'
            )
          ) {
            personalDay = day;
          }
        });
        this.props.publicHolidays.forEach(day => {
          if (
            day.date ===
            moment(new Date(this.currentYear, this.currentMonth - 1, j)).format(
              'YYYY-MM-DD'
            )
          ) {
            publicHoliday = day;
          }
        });

        line.push(
          <TeamCalendarUserTd key={'TeamCalendarUserTd' + j}>
            <TeamCalendarDay
              weekDay={
                weekDays[
                  new Date(this.currentYear, this.currentMonth, j).getDay()
                ]
              }
              requestStatus={personalDay && personalDay.requestStatus}
            >
              {publicHoliday ? (
                <img src={HolidayImg} />
              ) : personalDay ? (
                <img src={leaveTypes[personalDay.absenceType]} />
              ) : (
                j
              )}
            </TeamCalendarDay>
          </TeamCalendarUserTd>
        );
      }
      days.push(
        <TeamCalendarUserTr key={'TeamCalendarUserTr' + user.id}>
          {line}
        </TeamCalendarUserTr>
      );
    });

    return days;
  }

  renderTotals = () => {
    var totals = [];
    this.props.users.forEach(user => {
      totals.push(
        <tr key={'TotalsTr' + user.id}>
          <TeamCalendarTotalsTd key={'TeamCalendarTotalsTd0' + user.id}>
            0
          </TeamCalendarTotalsTd>
          <TeamCalendarTotalsTd key={'TeamCalendarTotalsTd1' + user.id}>
            0
          </TeamCalendarTotalsTd>
          <TeamCalendarTotalsTd key={'TeamCalendarTotalsTd2' + user.id}>
            0
          </TeamCalendarTotalsTd>
          <TeamCalendarTotalsTd key={'TeamCalendarTotalsTd3' + user.id}>
            0
          </TeamCalendarTotalsTd>
        </tr>
      );
    });
    return totals;
  }
  renderGridCalendar = () => {
    var grid = [];
    for (var j = 0; j < 31; ) {
      var row = [];
      for (var i = 0; i < 7; i++) {
        j++;
        row.push(
          <TeamCalendarGridTd key={'TeamCalendarGridTd' + i}>
            /* {i == 0 ? <div /> : ''} */
          </TeamCalendarGridTd>
        );
      }
      grid.push(<tr key={'TeamCalendarGridTr' + j}>{row}</tr>);
    }
    return grid;
  }

  render = () => {
    return (
      <TeamCalendarWrapper>
        <TeamCalendarMenus>
          <select>
            <option>All Users</option>
            <option>My Team</option>
          </select>

          <TeamCalendarViewButton> List View </TeamCalendarViewButton>
          <TeamCalendarViewButton> Calendar View </TeamCalendarViewButton>

          <TeamCalendarNavButton> {'<'} </TeamCalendarNavButton>
          <TeamCalendarNavSelect>
            <option>January 2019</option>
            <option>Febriary 2019</option>
          </TeamCalendarNavSelect>
          <TeamCalendarNavButton> {'>'} </TeamCalendarNavButton>
        </TeamCalendarMenus>

        <TeamCalendarContainer>
          <table>
            <tbody>
              <tr>
                <th>Monday</th>
                <th>Tuesday</th>
                <th>Wednesday</th>
                <th>Thursday</th>
                <th>Friday</th>
                <th>Saturday</th>
                <th>Sunday</th>
              </tr>
              {this.renderGridCalendar()}
            </tbody>
          </table>
        </TeamCalendarContainer>

        <TeamCalendarContainer>
          <TeamUsersContainer>{this.renderTeamUsers()}</TeamUsersContainer>
          <TeamDaysTable>
            <tbody>
              <tr>{this.renderDaysHeader()}</tr>
              {this.renderDays()}
            </tbody>
          </TeamDaysTable>

          <TeamTotalsTable>
            <tbody>
              <tr>
                <TeamCalendarTotalsTh>
                  {' '}
                  Total Working Days{' '}
                </TeamCalendarTotalsTh>
                <TeamCalendarTotalsTh> Total Leave Days </TeamCalendarTotalsTh>
                <TeamCalendarTotalsTh>
                  {' '}
                  Total Non Deducted Days{' '}
                </TeamCalendarTotalsTh>
                <TeamCalendarTotalsTh> Total Sick Days </TeamCalendarTotalsTh>
              </tr>
              {this.renderTotals()}
            </tbody>
          </TeamTotalsTable>
        </TeamCalendarContainer>
      </TeamCalendarWrapper>
    );
  }
}

const mapStateToProps = state => ({
  users: getUsers(state),
  publicHolidays: getPublicHolidays(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators(
    {
      requestUsers,
      requestPublicHolidays
    },
    dispatch
  );

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TeamCalendar);
