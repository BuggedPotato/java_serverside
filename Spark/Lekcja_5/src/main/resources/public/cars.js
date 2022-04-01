function init()
{
    console.log( "init" )
    loadCars();
}

async function loadCars()
{
    let cars = await asyncFetchJSON( "/json" );
    console.log( cars );
    const table = document.getElementById( "cars" );
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
    return row;
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