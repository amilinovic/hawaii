import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import styled from 'styled-components';
import { ifProp, prop, switchProp } from 'styled-tools';
import HolidayImg from '../../img/holiday.svg';
import { toggleModal } from '../../store/actions/modalActions';

// TODO: Handle overriding better with styled-components
const DayCell = styled.td`
  border: ${ifProp('isToday', '1px solid grey', '1px solid #c0c0c0')};
  border-style: ${ifProp('isToday', 'double')};
  min-width: 20px;
  font-size: 14px;
  border-color: ${ifProp('isToday', 'grey')};
  background-color: ${ifProp('disabled', '#9e9e9e')};
  background-color: ${ifProp('isWeekend', '#E0E0E0')};
  pointer-events: ${props => (props.disabled || props.isWeekend ? 'none' : '')};
  cursor: pointer;
  &:hover {
    background-color: rgba(0, 0, 0, 0.5);
  }
  background-color: ${switchProp(prop('requestStatus'), {
    APPROVED: 'green',
    PENDING: 'orange'
  })};
  content: ${prop('requestStatus')};
`;

const Image = styled.img`
  width: 15px;
  height: 15px;
`;

class Day extends Component {
  toggle = day => {
    this.props.toggleModal(day);
  };

  render() {
    return (
      <DayCell
        onClick={() => this.toggle(this.props.day)}
        className="text-center align-middle"
        {...this.props}
      >
        {this.props.publicHoliday && (
          <Image src={HolidayImg} alt="public_holiday" />
        )}
        {this.props.personalDay && (
          <Image src={this.props.personalDay.iconUrl} />
        )}
      </DayCell>
    );
  }
}

const mapDispatchToProps = dispatch =>
  bindActionCreators({ toggleModal }, dispatch);

export default connect(
  null,
  mapDispatchToProps
)(Day);
