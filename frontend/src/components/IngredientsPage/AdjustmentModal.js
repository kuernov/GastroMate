import React from "react";
import { Modal, Form, Radio, Input } from "antd";

const AdjustmentModal = ({ visible, onCancel, onSubmit, form }) => (
    <Modal
        open={visible}
        title={<h3 style={{ textAlign: 'center', color: '#4a90e2' }}>Adjust Inventory</h3>}
        onCancel={onCancel}
        onOk={onSubmit}
        okText="Confirm Adjustment"
        cancelText="Cancel"
        centered
    >
        <div style={{ padding: '20px 30px' }}>
            <Form form={form} layout="vertical">
                <Form.Item
                    name="changeType"
                    label={<span style={{ fontWeight: 'bold' }}>Adjustment Type</span>}
                    rules={[{ required: true, message: 'Please select an adjustment type' }]}
                >
                    <Radio.Group style={{ display: 'flex', justifyContent: 'space-around' }}>
                        <Radio.Button value="add" style={{ width: '100%', textAlign: 'center' }}>
                            <span role="img" aria-label="Add">➕ Add</span>
                        </Radio.Button>
                        <Radio.Button value="subtract" style={{ width: '100%', textAlign: 'center' }}>
                            <span role="img" aria-label="Subtract">➖ Subtract</span>
                        </Radio.Button>
                        <Radio.Button value="adjust" style={{ width: '100%', textAlign: 'center' }}>
                            <span role="img" aria-label="Adjust">⚙️ Adjust</span>
                        </Radio.Button>
                    </Radio.Group>
                </Form.Item>
                <Form.Item
                    name="quantity"
                    label={<span style={{ fontWeight: 'bold' }}>Quantity</span>}
                    rules={[{ required: true, message: 'Please enter a quantity' }]}
                >
                    <Input
                        type="number"
                        placeholder="Enter quantity"
                        prefix={<span role="img" aria-label="Quantity"></span>}
                        style={{ width: '100%' }}
                    />
                </Form.Item>
            </Form>
        </div>
    </Modal>
);

export default AdjustmentModal;
