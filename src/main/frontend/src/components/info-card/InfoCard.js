import React, { Component } from 'react';
import styled from 'styled-components';
import Loading from '../loading/Loading';

const Card = styled.div`
  background: white;
`;

export default class InfoCard extends Component {
  render() {
    if (!this.props.allowance) return <Loading />;

    const cardInfo = this.props.allowance.map(item => {
      return (
        <div key={item.id} className="d-flex justify-content-between mb-2">
          <span>{item.type}</span>
          <span>{item.hours} hours</span>
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
