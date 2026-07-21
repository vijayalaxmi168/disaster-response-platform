import React from 'react'

export default function StatusBadge({ status }) {
  const cls =
    status === 'PENDING' ? 'badge badge-pending' :
    status === 'ASSIGNED' ? 'badge badge-assigned' :
    'badge badge-completed'
  return <span className={cls}>{status}</span>
}
