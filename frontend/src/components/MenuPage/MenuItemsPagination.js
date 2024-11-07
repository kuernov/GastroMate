import React from "react";
import { Pagination } from "antd";

const MenuItemsPagination = ({ page, pageSize, totalItems, onPageChange }) => {
    return (
        <Pagination
            current={page}
            pageSize={pageSize}
            total={totalItems}
            onChange={onPageChange}
            style={{ marginTop: "20px", textAlign: "center" }}
            showSizeChanger={false} // Hides the page size changer if you want to keep it fixed
        />
    );
};

export default MenuItemsPagination;
