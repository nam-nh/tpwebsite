import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './item.reducer';
import { IItem } from 'app/shared/model/item.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IItemDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ItemDetail extends React.Component<IItemDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { itemEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="tpwebsiteApp.item.detail.title">Item</Translate> [<b>{itemEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="itemName">
                <Translate contentKey="tpwebsiteApp.item.itemName">Item Name</Translate>
              </span>
            </dt>
            <dd>{itemEntity.itemName}</dd>
            <dt>
              <span id="itemImage">
                <Translate contentKey="tpwebsiteApp.item.itemImage">Item Image</Translate>
              </span>
            </dt>
            <dd>{itemEntity.itemImage}</dd>
            <dt>
              <span id="itemDescription">
                <Translate contentKey="tpwebsiteApp.item.itemDescription">Item Description</Translate>
              </span>
            </dt>
            <dd>{itemEntity.itemDescription}</dd>
            <dt>
              <span id="itemQuantity">
                <Translate contentKey="tpwebsiteApp.item.itemQuantity">Item Quantity</Translate>
              </span>
            </dt>
            <dd>{itemEntity.itemQuantity}</dd>
            <dt>
              <span id="itemPrice">
                <Translate contentKey="tpwebsiteApp.item.itemPrice">Item Price</Translate>
              </span>
            </dt>
            <dd>{itemEntity.itemPrice}</dd>
            <dt>
              <Translate contentKey="tpwebsiteApp.item.itemSubGroup">Item Sub Group</Translate>
            </dt>
            <dd>{itemEntity.itemSubGroup ? itemEntity.itemSubGroup.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/item" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/item/${itemEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ item }: IRootState) => ({
  itemEntity: item.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ItemDetail);
