import React, { Component } from 'react';
import styled from 'styled-components';
import Loading from '../loading/Loading';

const Card = styled.div`
  background: white;
`;

const title = ['Taken', 'Pending', 'Remaining'];

export default class InfoCard extends Component {
  render() {
    if (!this.props.allowance) return <Loading />;

    const cardInfo = this.props.allowance.map((item, index) => {
      return (
        <div key={item.id} className="d-flex justify-content-between mb-2">
          <span>{title[index]}</span>
          <span>{item.hours / 8} days</span>
        </div>
      );
    });

    return (
      <Card className="p-3 border rounded">
        <h1 className="border-bottom mb-3 pb-1">{this.props.title}</h1>
        {cardInfo}
      </Card>
    );
  }
}
