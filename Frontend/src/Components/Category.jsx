import '../Styles/Category.css';

const Category = ({ id, name, isChecked, onMark }) => {

    const handleMark = (event) => {
        onMark(id, event.target.checked);
    }

    return (
        <div className="category">
            <input type="checkbox" className="category__checkbox" id={id} checked={isChecked} onChange={handleMark} />
            <label className="category__name" htmlFor={id}>
                {name}
            </label>
        </div>
    );

}

export default Category;