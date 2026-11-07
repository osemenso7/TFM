import '../Styles/Category.css';

const Category = ({ id, name, onMark }) => {

    const handleMark = (event) => {
        onMark(name, event.target.checked);
    }

    return (
        <div className="category">
            <input type="checkbox" className="category__checkbox" id={id} onChange={handleMark} />
            <label className="category__name" htmlFor={id}>
                {name}
            </label>
        </div>
    );

}

export default Category;