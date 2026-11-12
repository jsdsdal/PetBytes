function StatBar({ label, value }) {

    let level = 'good'
    if (value < 50) level = 'medium'
    if (value < 25) level = 'low'

    return (
        <div className="stat">
            <span className="stat-label">{label}</span>
            <progress className={'stat-bar ' + level} value={value} max="100">
                {value}
            </progress>
            <span className="stat-value">{value}</span>
        </div>
    )
}

export default StatBar