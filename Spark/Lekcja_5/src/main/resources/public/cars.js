function init()
{
    loadCars();
}

async function loadCars()
{
    const table = document.getElementById( "cars" );
    const children = [ ...table.children ];
    for( let i = 0; i < children.length; i++ )
    {
        console.log( children[i] );
        table.removeChild( children[i] );
    }
    let cars = await asyncFetchJSON( "/json" );
    console.log( cars );
    cars.map( ( car, i ) =>{
        table.appendChild( tableRow( car ) );
    } );
}

function tableRow( car )
{
    let row = document.createElement( "tr" );
    Object.keys( car ).map( ( key, i )=> {
        let cell = document.createElement( "td" );
        let text = "";
        if( Array.isArray( car[ key ] ) )
        {
            car[ key ].map( ( el )=>{ text += el.name + ": "; text += el.value + "\n" } );
        }
        else if( key === "colour" )
        {
            cell.style.backgroundColor = car[ key ];
        }
        else
            text = car[ key ];
        cell.innerText = text;
        row.appendChild( cell );
    } );
    let cell = document.createElement( "td" );
    let btn = document.createElement( "button" );
    btn.innerText = "Edytuj";
    btn.addEventListener( "click", async ()=>{ editDialog( car ) } )
    cell.appendChild( btn );
    row.appendChild( cell );

    cell = document.createElement( "td" );
    btn = document.createElement( "button" );
    btn.innerText = "Usuń";
    btn.addEventListener( "click", async ()=>{ await asyncFetchJSON( "/delete", car.uuid ); await loadCars() } )
    cell.appendChild( btn );
    row.appendChild( cell );

    return row;
}

function editDialog( car )
{
    const dialog = document.getElementById( "update-dialog" );
    const cover = document.getElementById( "cover" );
    const modelInput = document.getElementById( "model" );
    const yearInput = document.getElementById( "year" );
    modelInput.value = car.model;
    yearInput.value = car.year;
    dialog.style.visibility = "visible";
    cover.style.visibility = "visible";

    document.getElementById( "update-button" ).onclick = async () => {
        car.model = modelInput.value;
        car.year = yearInput.value;
        await asyncFetchJSON( "/update", car );
        dialog.style.visibility = "hidden";
        cover.style.visibility = "hidden";
        modelInput.value = "";
        yearInput.value = 2000;
        loadCars();
    }

    document.getElementById( "cancel-button" ).onclick = () => {
        dialog.style.visibility = "hidden";
        cover.style.visibility = "hidden";
        modelInput.value = "";
        yearInput.value = 2000;
    }
}

async function asyncFetchJSON( target, data = {} )
{
    const options = {
        method: "POST",
        body: JSON.stringify( data )
    };
    let res = await fetch( target, options );
    if( !res.ok )
        return res.status;
    return await res.json();
}