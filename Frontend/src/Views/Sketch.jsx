import { useState, useRef } from 'react';
import { requestImportCsv } from '../Services/ImportService';

import Category from '../Components/Category';
import useAuth from '../Hooks/UseAuth';

import '../Styles/Sketch.css';

const Sketch = () => {

    const { token } = useAuth();
    const fileInputRef = useRef(null);
    const [categoriesMarked, setCategoriesMarked] = useState([]);
    const [importing, setImporting] = useState(false);
    const [error, setError] = useState('');

    const categories = [
        { id: 1, name: 'Luz' },
        { id: 2, name: 'Gas' },
        { id: 3, name: 'Hipoteca' },
        { id: 4, name: 'Suscripciones' },
        { id: 5, name: 'Comida' }
    ];

    const handleCategoriesSelected = (categoryName, isChecked) => {
        setCategoriesMarked((currentCategoriesMarked) =>
            isChecked
                ? [...currentCategoriesMarked, categoryName]
                : currentCategoriesMarked.filter((category) => category !== categoryName)
        );
    }

    const handleExport = () => {
        // Aquí puedes implementar la lógica para exportar los datos de las categorías seleccionadas
        console.log('Exportando categorías:', categoriesMarked);
    }

    const handleImport = () => {
        fileInputRef.current.click();
    }

    const handleFileSelected = async (event) => {
        setError('');
        setImporting(true);

        const file = event.target.files?.[0];

        if (!file) {
            return;
        }

        try {
            await requestImportCsv(file, token);
            console.log('CSV importado correctamente');
        } catch (error) {
            setError(error.message || 'Invalid file');
        } finally {
            setIsImporting(false);
            event.target.value = '';
        }
    };

    const handleSave = () => {
        // Aquí puedes implementar la lógica para importar los datos de las categorías seleccionadas
        console.log('Importando categorías:', categoriesMarked);
    }

    return (
        <div className="sketch">
            <div className="sketch__categories">  
                <h2 className="categories__title">Categories</h2>
                <div className="categories__list">  
                    {categories
                        .map((category) => (
                        <Category key={category.id} id={category.id} name={category.name} onMark={handleCategoriesSelected} />
                    ))}
                </div>
            </div>

            <div className="sketch__workspace">
                <div className="workspace__header"> 
                    <input type="search" className="header__searchBar" placeholder=" Buscar..."/>
                    <div className="header__buttons"> 
                        <button type="button" className="buttons__exportBtn" onClick={handleExport}>
                            <span className="import__text"> Exportar </span>
                        </button>
                        <button type="button" className="buttons__importBtn" onClick={handleImport} disabled={importing}>
                            <span className="import__text"> Importar </span>
                        </button>
                        <button type="button" className="buttons__importBtn" onClick={handleSave}>
                            <span className="import__text"> Guardar </span>
                        </button>
                    </div>

                    <input type="file" className="importBtn__file" ref={fileInputRef} accept=".csv" onChange={handleFileSelected} hidden/>
                </div>
                <div className="categories__list"> 
                    {error && <p className="import__error">{error}</p>} 
                    {categoriesMarked
                        .map((category, index) => (
                            <span key={index} className="category__selected">
                                {category}
                            </span>
                        ))}
                </div>
            </div>
        </div>
    );
};

export default Sketch;