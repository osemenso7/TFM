// import { useState, useRef } from 'react';
import { useState, useRef, useEffect } from 'react';
import { requestImportCsv } from '../Services/ImportService';
import { saveSelectedCategories } from '../Services/CategoriesService';

import Category from '../Components/Category';
import useAuth from '../Hooks/UseAuth';
import useCategories from '../Hooks/UseCategories';
import useCategoriesMarked from '../Hooks/UseCategoriesMarked';

import '../Styles/Sketch.css';

const Sketch = () => {

    const fileInputRef = useRef(null);

    const { token } = useAuth();
    const { categories, selectedCategories, categoriesError } = useCategories(token);
    const { categoriesMarked, handleCategoriesSelected } = useCategoriesMarked(selectedCategories);

    const [processing, setProcessing] = useState(false);
    const [error, setError] = useState('');

    const handleImport = () => {
        fileInputRef.current.click();
    }

    const handleFileSelected = async (event) => {
        setError('');
        setProcessing(true);

        const file = event.target.files?.[0];

        if (!file) {
            setProcessing(false);
            return;
        }

        try {
            await requestImportCsv(file, token);
        } catch (error) {
            setError(error.message || 'Invalid file');
        } finally {
            setProcessing(false);
            event.target.value = '';
        }
    };

    const handleSave = async () => {
        setError('');
        setProcessing(true);

        try {
            await handleSaveCategories();

            // Pending: Save Import para dashboard
        } catch (error) {
            setError(error.message || 'Save error');
        } finally {
            setProcessing(false);
        }
    };

    const handleSaveCategories = async () => {
        await saveSelectedCategories(token, categoriesMarked);
    };

    const handleExport = () => {
        // Aquí puedes implementar la lógica para exportar los datos de las categorías seleccionadas
        console.log('Exportando categorías:', categoriesMarked);
    }

    return (
        <div className="sketch">
            <div className="sketch__categories">  
                <h2 className="categories__title">Categories</h2>
                <div className="categories__list">  
                    {categories
                        .map((category) => (
                        <Category key={category.id} id={category.id} name={category.name} isChecked={categoriesMarked.includes(category.id)} onMark={handleCategoriesSelected} />
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
                        <button type="button" className="buttons__importBtn" onClick={handleImport} disabled={processing}>
                            <span className="import__text"> Importar </span>
                        </button>
                        <button type="button" className="buttons__importBtn" onClick={handleSave} disabled={processing}>
                            <span className="import__text"> Guardar </span>
                        </button>
                    </div>

                    <input type="file" className="importBtn__file" ref={fileInputRef} accept=".csv" onChange={handleFileSelected} hidden/>
                </div>
                <div className="workspace__table"> 
                    {error && <p className="import__error">{error}</p>} 
                    <table className="table_import">
                        <tbody>
                            {categories
                                .filter((category) => categoriesMarked.includes(category.id))
                                .map((category) => (
                                <tr key={category.id}>
                                    <td>{category.name}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
};

export default Sketch;